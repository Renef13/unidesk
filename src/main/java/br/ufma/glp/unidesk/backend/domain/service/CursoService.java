package br.ufma.glp.unidesk.backend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ufma.glp.unidesk.backend.domain.exception.CursoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import br.ufma.glp.unidesk.backend.domain.repository.CursoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Service
public class CursoService {
    
    private CursoRepository cursoRepository;

    CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    @Transactional
    public Curso criarCurso(@Valid @NotBlank(message = "Os dados do curso nao devem estar vazios") Curso curso) {
        Curso novoCurso = cursoRepository.save(curso);
        return novoCurso;
    }

    @Transactional
    public Curso alterarCurso(@Valid @NotNull(message = "O id do curso nao pode ser nulo") Long idCurso, Curso curso) {
        Curso cursoAlterado = cursoRepository.findById(idCurso).orElseThrow(() -> new CursoNaoEncontradoException(idCurso));
        if(curso.getCampus() != null) {
            cursoAlterado.setCampus(curso.getCampus());
        }
        if(curso.getNome() != null) {
            cursoAlterado.setNome(curso.getNome());
        }
        return cursoAlterado;
    }

    @Transactional
    public void desativarCurso(@Valid @NotNull(message = "O id do curso nao pode ser nulo") Long idCurso) {
        Curso cursoParaDeletar = cursoRepository.findById(idCurso).orElseThrow(() -> new CursoNaoEncontradoException(idCurso));

        cursoRepository.delete(cursoParaDeletar);
    }

    public Curso buscarCursoPorId(@Valid @NotNull(message = "O id do curso nao pode ser nulo") Long idCurso) {
        return cursoRepository.findById(idCurso).orElseThrow(() -> new CursoNaoEncontradoException(idCurso));
    }


}
