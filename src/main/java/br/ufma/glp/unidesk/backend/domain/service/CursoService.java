package br.ufma.glp.unidesk.backend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ufma.glp.unidesk.backend.domain.exception.CursoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.repository.CursoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    @Transactional
    public Curso criarCurso(@Valid @NotBlank(message = "Os dados do curso não devem estar vazios") Curso curso) {
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso alterarCurso(@Valid @NotNull(message = "O id do curso não pode ser nulo") Long idCurso, Curso curso) {
        Curso cursoExistente = buscarCursoPorId(idCurso);
        atualizarDadosCurso(cursoExistente, curso);
        return cursoRepository.save(cursoExistente);
    }

    @Transactional
    public void desativarCurso(Usuario usuario, @Valid @NotNull(message = "O id do curso não pode ser nulo") Long idCurso) {
        validarPermissaoAdmin(usuario);
        Curso cursoParaDeletar = buscarCursoPorId(idCurso);
        cursoRepository.delete(cursoParaDeletar);
    }

    public Curso buscarCursoPorId(@Valid @NotNull(message = "O id do curso não pode ser nulo") Long idCurso) {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new CursoNaoEncontradoException(idCurso));
    }

    public List<Curso> buscarCursoPorNome(String nome) {
        List<Curso> cursos = cursoRepository.findByNomeContainingIgnoreCase(nome);
        if (cursos.isEmpty()) {
            throw new CursoNaoEncontradoException("Não encontrado cursos com o nome " + nome);
        }
        return cursos;
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
}