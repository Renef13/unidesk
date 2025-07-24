package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.CoordenacaoEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.CoordenacaoNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.exception.CoordenacaoSemCursoException;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import br.ufma.glp.unidesk.backend.domain.repository.CoordenacaoRepository;
import br.ufma.glp.unidesk.backend.domain.repository.CursoRepository;
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
public class CoordenacaoService {

    private final CoordenacaoRepository coordenacaoRepository;
    private final CursoRepository cursoRepository;

    public List<Coordenacao> listarTodas() {
        return coordenacaoRepository.findAll();
    }

    public Coordenacao buscarPorIdOuFalhar(@NotNull Long idCoordenacao) {
        return coordenacaoRepository.findByIdCoordenacao(idCoordenacao)
                .orElseThrow(() -> new CoordenacaoNaoEncontradaException(idCoordenacao));
    }

    public List<Coordenacao> buscarPorNome(String nome) {
        List<Coordenacao> coordenacoes = coordenacaoRepository.findByNomeContainingIgnoreCase(nome);
        if (coordenacoes.isEmpty()) {
            throw new CoordenacaoNaoEncontradaException("Nenhuma coordenação encontrada com o nome: " + nome);
        }
        return coordenacoes;
    }

    public List<Coordenacao> buscarPorCurso(Long idCurso) {
        return coordenacaoRepository.findByCursoIdCurso(idCurso);
    }

    @Transactional
    public Coordenacao salvar(@Valid @NotNull Coordenacao coordenacao) {
        validarCurso(coordenacao);
        if (coordenacaoRepository.existsByNome(coordenacao.getNome())) {
            throw new IllegalArgumentException("Já existe uma coordenação com esse nome");
        }
        return coordenacaoRepository.save(coordenacao);
    }

    @Transactional
    public Coordenacao atualizar(@NotNull Long idCoordenacao, @Valid @NotNull Coordenacao coordenacao) {
        Coordenacao existente = buscarPorIdOuFalhar(idCoordenacao);
        validarCurso(coordenacao);

        existente.setNome(coordenacao.getNome());
        existente.setCurso(coordenacao.getCurso());

        return coordenacaoRepository.save(existente);
    }

    @Transactional
    public void excluir(@NotNull Long idCoordenacao) {
        Coordenacao coordenacao = buscarPorIdOuFalhar(idCoordenacao);
        try {
            coordenacaoRepository.delete(coordenacao);
            coordenacaoRepository.flush();
        } catch (Exception e) {
            throw new CoordenacaoEmUsoException(idCoordenacao);
        }
    }

    private void validarCurso(Coordenacao coordenacao) {
        if (coordenacao.getCurso() == null || coordenacao.getCurso().getIdCurso() == null) {
            throw new CoordenacaoSemCursoException();
        }
        Curso curso = cursoRepository.findById(coordenacao.getCurso().getIdCurso())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado para o id: " + coordenacao.getCurso().getIdCurso()));
        coordenacao.setCurso(curso);
    }
}