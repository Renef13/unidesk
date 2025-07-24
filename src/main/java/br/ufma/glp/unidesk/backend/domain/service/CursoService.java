package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.CursoEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.CursoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.CursoNomeEmUsoException;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.repository.CursoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CursoService {

    private final AlunoService alunoService;
    private final CoordenacaoService coordenacaoService;
    private final CursoRepository cursoRepository;

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso buscarCursoPorId(@NotNull(message = "O id do curso não pode ser nulo") Long idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new CursoNaoEncontradoException(idCurso));
    }

    public List<Curso> buscarCursoPorNome(@NotBlank(message = "O nome do curso não pode ser vazio") String nome) {
        List<Curso> cursos = cursoRepository.findByNomeContainingIgnoreCase(nome);
        if (cursos.isEmpty()) {
            throw new CursoNaoEncontradoException("Não encontrado cursos com o nome " + nome);
        }
        return cursos;
    }

    @Transactional
    public Curso criarCurso(@Valid @NotNull(message = "O curso não pode ser nulo") Curso curso) {
        validarNomeEmUso(curso.getNome());
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso alterarCurso(@NotNull(message = "O id do curso não pode ser nulo") Long idCurso, @Valid @NotNull(message = "O curso não pode ser nulo") Curso curso) {
        Curso cursoExistente = buscarCursoPorId(idCurso);
        if (!cursoExistente.getNome().equalsIgnoreCase(curso.getNome())) {
            validarNomeEmUso(curso.getNome());
        }
        atualizarDadosCurso(cursoExistente, curso);
        return cursoRepository.save(cursoExistente);
    }

    @Transactional
    public void desativarCurso(@NotNull(message = "O id do curso não pode ser nulo") Long idCurso) {
        //validarPermissaoAdmin(usuario);
        Curso cursoParaDeletar = buscarCursoPorId(idCurso);
        validarCursoEmUso(cursoParaDeletar);
        cursoRepository.delete(cursoParaDeletar);
    }

    private void atualizarDadosCurso(Curso cursoExistente, Curso novosDados) {
        if (novosDados.getCampus() != null) {
            cursoExistente.setCampus(novosDados.getCampus());
        }
        if (novosDados.getNome() != null) {
            cursoExistente.setNome(novosDados.getNome());
        }
    }

    private void validarPermissaoAdmin(Usuario usuario) {
        boolean isAdmin = usuario.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().contains("ADMIN"));
        if (!isAdmin) {
            throw new SecurityException("Usuário não possui permissão para desativar cursos.");
        }
    }

    private void validarNomeEmUso(String nome) {
        if (cursoRepository.existsByNome(nome)) {
            throw new CursoNomeEmUsoException(nome);
        }
    }

    private void validarCursoEmUso(Curso curso) {
        if (alunoService.existePorCuso(curso.getIdCurso())) {
            throw new CursoEmUsoException("Curso não pode ser removido, pois está em uso por alunos.");
        }
        if (coordenacaoService.existePorCurso(curso.getIdCurso())) {
            throw new CursoEmUsoException("Curso não pode ser removido, pois está em uso por coordenações.");
        }
    }
}