package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.FuncionarioCoordenacaoEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.FuncionarioCoordenacaoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.FuncionarioCoordenacaoSemCoordenacaoException;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import br.ufma.glp.unidesk.backend.domain.repository.FuncionarioCoordenacaoRepository;
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
public class FuncionarioCoordenacaoService {

    private final FuncionarioCoordenacaoRepository funcionarioCoordenacaoRepository;
    private final CoordenacaoService coordenacaoService;

    public List<FuncionarioCoordenacao> listarTodos() {
        return funcionarioCoordenacaoRepository.findAll();
    }

    public FuncionarioCoordenacao buscarPorIdOuFalhar(@NotNull Long idFuncionario) {
        return funcionarioCoordenacaoRepository.findById(idFuncionario)
                .orElseThrow(() -> new FuncionarioCoordenacaoNaoEncontradoException(idFuncionario));
    }

    public FuncionarioCoordenacao buscarPorMatriculaOuFalhar(@NotNull String matricula) {
        return funcionarioCoordenacaoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new FuncionarioCoordenacaoNaoEncontradoException(matricula, "FUNCIONARIO"));
    }

    public List<FuncionarioCoordenacao> buscarPorCoordenacao(Long idCoordenacao) {
        return funcionarioCoordenacaoRepository.findByCoordenacaoIdCoordenacao(idCoordenacao);
    }

    public List<FuncionarioCoordenacao> buscarPorNome(String nome) {
        List<FuncionarioCoordenacao> funcionarios = funcionarioCoordenacaoRepository.findByNomeContainingIgnoreCase(nome);
        if (funcionarios.isEmpty()) {
            throw new FuncionarioCoordenacaoNaoEncontradoException("Nenhum funcionário encontrado com o nome: " + nome);
        }
        return funcionarios;
    }

    @Transactional
    public FuncionarioCoordenacao salvar(@Valid @NotNull FuncionarioCoordenacao funcionario) {
        validarCoordenacao(funcionario);
        if (funcionarioCoordenacaoRepository.existsByMatricula(funcionario.getMatricula())) {
            throw new IllegalArgumentException("Já existe um funcionário com essa matrícula");
        }
        Coordenacao coordenacao = coordenacaoService.buscarPorIdOuFalhar(funcionario.getCoordenacao().getIdCoordenacao());
        funcionario.setCoordenacao(coordenacao);
        return funcionarioCoordenacaoRepository.save(funcionario);
    }

    @Transactional
    public FuncionarioCoordenacao atualizar(@NotNull Long idFuncionario, @Valid @NotNull FuncionarioCoordenacao funcionario) {
        FuncionarioCoordenacao existente = buscarPorIdOuFalhar(idFuncionario);

        if (!existente.getMatricula().equals(funcionario.getMatricula()) &&
                funcionarioCoordenacaoRepository.existsByMatricula(funcionario.getMatricula())) {
            throw new IllegalArgumentException("Já existe um funcionário com essa matrícula");
        }

        validarCoordenacao(funcionario);
        Coordenacao coordenacao = coordenacaoService.buscarPorIdOuFalhar(funcionario.getCoordenacao().getIdCoordenacao());

        existente.setNome(funcionario.getNome());
        existente.setEmail(funcionario.getEmail());
        existente.setSenha(funcionario.getSenha());
        existente.setMatricula(funcionario.getMatricula());
        existente.setCoordenacao(coordenacao);

        return funcionarioCoordenacaoRepository.save(existente);
    }

    @Transactional
    public void remover(@NotNull Long idFuncionario) {
        FuncionarioCoordenacao funcionario = buscarPorIdOuFalhar(idFuncionario);
        try {
            funcionarioCoordenacaoRepository.delete(funcionario);
            funcionarioCoordenacaoRepository.flush();
        } catch (Exception e) {
            throw new FuncionarioCoordenacaoEmUsoException(idFuncionario);
        }
    }

    private void validarCoordenacao(FuncionarioCoordenacao funcionario) {
        if (funcionario.getCoordenacao() == null || funcionario.getCoordenacao().getIdCoordenacao() == null) {
            throw new FuncionarioCoordenacaoSemCoordenacaoException();
        }
    }
}