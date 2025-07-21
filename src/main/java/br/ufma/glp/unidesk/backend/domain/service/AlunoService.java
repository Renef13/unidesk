package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.AlunoEmailEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.AlunoMatriculaEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.AlunoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.CursoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import br.ufma.glp.unidesk.backend.domain.repository.AlunoRepository;
import br.ufma.glp.unidesk.backend.domain.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno buscarPorIdOuFalhar(Long idAluno) {
        return alunoRepository.findById(idAluno)
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }

    public Aluno buscarPorMatriculaOuFalhar(String matricula) {
        return alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new AlunoNaoEncontradoException("Matrícula não encontrada: " + matricula));
    }

    public Aluno buscarPorEmailOuFalhar(String email) {
        return alunoRepository.findByEmail(email)
                .orElseThrow(() -> new AlunoNaoEncontradoException("Email não encontrado: " + email));
    }

    public Aluno buscarPorNomeOuFalhar(String nome) {
        return alunoRepository.findByNome(nome)
                .orElseThrow(() -> new AlunoNaoEncontradoException("Aluno não encontrado: " + nome));
    }

    public boolean existePorEmailOuMatricula(String email, String matricula) {
        return alunoRepository.existsByEmailOrMatricula(email, matricula);
    }

    private boolean existePorMatricula(String matricula) {
        return alunoRepository.findByMatricula(matricula).isPresent();
    }

    private void validarMatriculaEmUso(String matricula) {
        if (existePorMatricula(matricula)) {
            throw new AlunoMatriculaEmUsoException(matricula);
        }
    }

    private boolean existePorEmail(String email) {
        return alunoRepository.findByEmail(email).isPresent();
    }

    private void validarEmailEmUso(String email) {
        if (existePorEmail(email)) {
            throw new AlunoEmailEmUsoException(email);
        }
    }

    private void validarCurso(Aluno aluno) {
        if (aluno.getCurso() == null || aluno.getCurso().getIdCurso() == null) {
            throw new IllegalArgumentException("Curso do aluno não pode ser nulo");
        }
        Curso curso = cursoRepository.findByIdCurso(aluno.getCurso().getIdCurso())
                .orElseThrow(() -> new CursoNaoEncontradoException("Curso não encontrado para o id: " + aluno.getCurso().getIdCurso()));
        aluno.setCurso(curso);
    }

    public Aluno salvar(Aluno aluno) {
        validarEmailEmUso(aluno.getEmail());
        validarMatriculaEmUso(aluno.getMatricula());
        validarCurso(aluno);
        return alunoRepository.save(aluno);
    }


}