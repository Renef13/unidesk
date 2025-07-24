package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.CoordenadorEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.CoordenadorInativoException;
import br.ufma.glp.unidesk.backend.domain.exception.CoordenadorNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.repository.CoordenadorRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CoordenadorService {

    private final CoordenadorRepository coordenadorRepository;
    private final CoordenacaoService coordenacaoService;

    public List<Coordenador> listarTodos() {
        return coordenadorRepository.findAll();
    }

    public Coordenador buscarPorIdOuFalhar(@NotNull Long idCoordenador) {
        return coordenadorRepository.findById(idCoordenador)
                .orElseThrow(() -> new CoordenadorNaoEncontradoException(idCoordenador));
    }

    public Coordenador buscarPorMatriculaOuFalhar(@NotNull String matricula) {
        return coordenadorRepository.findByMatricula(matricula)
                .orElseThrow(() -> new CoordenadorNaoEncontradoException(matricula, true));
    }

    public List<Coordenador> buscarPorCoordenacao(Long idCoordenacao) {
        return coordenadorRepository.findByCoordenacaoIdCoordenacao(idCoordenacao);
    }

    public List<Coordenador> buscarPorCoordenacaoEAtivo(Long idCoordenacao, Boolean ativo) {
        return coordenadorRepository.findByCoordenacaoIdCoordenacaoAndAtivo(idCoordenacao, ativo);
    }

    @Transactional
    public Coordenador salvar(@Valid @NotNull Coordenador coordenador) {
        if (coordenadorRepository.existsByMatricula(coordenador.getMatricula())) {
            throw new IllegalArgumentException("Já existe um coordenador com essa matrícula");
        }
        Coordenacao coordenacao = coordenacaoService.buscarPorIdOuFalhar(coordenador.getCoordenacao().getIdCoordenacao());

        if (coordenador.getAtivo() != null && coordenador.getAtivo()) {
            List<Coordenador> ativos = coordenadorRepository.findByCoordenacaoAndAtivo(coordenacao, true);
            if (!ativos.isEmpty()) {
                throw new IllegalArgumentException("Já existe um coordenador ativo para esta coordenação");
            }
        }

        coordenador.setCoordenacao(coordenacao);
        return coordenadorRepository.save(coordenador);
    }

    @Transactional
    public Coordenador atualizar(@NotNull Long idCoordenador, @Valid @NotNull Coordenador coordenador) {
        Coordenador existente = buscarPorIdOuFalhar(idCoordenador);

        if (!existente.getMatricula().equals(coordenador.getMatricula()) &&
                coordenadorRepository.existsByMatricula(coordenador.getMatricula())) {
            throw new IllegalArgumentException("Já existe um coordenador com essa matrícula");
        }

        Coordenacao coordenacao = coordenacaoService.buscarPorIdOuFalhar(coordenador.getCoordenacao().getIdCoordenacao());

        if (coordenador.getAtivo() != null && coordenador.getAtivo()) {
            List<Coordenador> ativos = coordenadorRepository.findByCoordenacaoAndAtivo(coordenacao, true);
            if (!ativos.isEmpty() && ativos.stream().noneMatch(c -> c.getIdUsuario().equals(idCoordenador))) {
                throw new IllegalArgumentException("Já existe um coordenador ativo para esta coordenação");
            }
        }

        existente.setNome(coordenador.getNome());
        existente.setEmail(coordenador.getEmail());
        existente.setSenha(coordenador.getSenha());
        existente.setAtivo(coordenador.getAtivo());
        existente.setMatricula(coordenador.getMatricula());
        existente.setCoordenacao(coordenacao);
        existente.setRole(coordenador.getRole());

        return coordenadorRepository.save(existente);
    }

    @Transactional
    public void excluir(@NotNull Long idCoordenador) {
        Coordenador coordenador = buscarPorIdOuFalhar(idCoordenador);
        try {
            coordenadorRepository.delete(coordenador);
            coordenadorRepository.flush();
        } catch (Exception e) {
            throw new CoordenadorEmUsoException(idCoordenador);
        }
    }

    @Transactional
    public Coordenador ativar(@NotNull Long idCoordenador) {
        Coordenador coordenador = buscarPorIdOuFalhar(idCoordenador);
        List<Coordenador> ativos = coordenadorRepository.findByCoordenacaoAndAtivo(coordenador.getCoordenacao(), true);
        if (!ativos.isEmpty() && ativos.stream().noneMatch(c -> c.getIdUsuario().equals(idCoordenador))) {
            throw new IllegalArgumentException("Já existe um coordenador ativo para esta coordenação");
        }
        coordenador.setAtivo(true);
        return coordenadorRepository.save(coordenador);
    }

    @Transactional
    public Coordenador desativar(@NotNull Long idCoordenador) {
        Coordenador coordenador = buscarPorIdOuFalhar(idCoordenador);
        coordenador.setAtivo(false);
        return coordenadorRepository.save(coordenador);
    }

    public void validarAtivo(Coordenador coordenador) {
        if (coordenador.getAtivo() == null || !coordenador.getAtivo()) {
            throw new CoordenadorInativoException(coordenador.getIdUsuario());
        }
    }
}