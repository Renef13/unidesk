package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.*;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.model.UsuarioRole;
import br.ufma.glp.unidesk.backend.domain.repository.AlunoRepository;
import br.ufma.glp.unidesk.backend.domain.repository.CursoRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final TicketRepository ticketRepository;
    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final CoordenadorService coordenadorService;
    private final FuncionarioCoordenacaoService funcionarioCoordenacaoService;

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno buscarPorIdOuFalhar(Long idAluno) {
        return alunoRepository.findById(idAluno)
                .orElseThrow(() -> new AlunoNaoEncontradoException(idAluno));
    }

    public List<Aluno> listarPorCursoAtual() {
        Usuario usuarioLogado = authService.getCurrentUsuarioEntity();
        if (usuarioLogado == null || usuarioLogado.getRole() == UsuarioRole.ALUNO) {
            throw new AlunoNaoEncontradoException("Usuário não autenticado ou é um aluno e não pode listar alunos por curso atual");
        }

        // Verifica o papel do usuário logado e busca os alunos do curso correspondente
        switch (usuarioLogado.getRole()) {
            // Coordenador pode ver alunos do curso que coordena
            case COORDENADOR:
                Curso cursoCoordenador = coordenadorService.buscarPorIdOuFalhar(usuarioLogado.getIdUsuario())
                        .getCoordenacao()
                        .getCurso();
                return alunoRepository.findByCurso(cursoCoordenador);
            // Funcionário de coordenação pode ver alunos do curso que tem acesso
            case FUNCIONARIO_COORDENACAO:
                Curso cursoFuncionario = funcionarioCoordenacaoService.buscarPorIdOuFalhar(usuarioLogado.getIdUsuario())
                        .getCoordenacao()
                        .getCurso();
                return alunoRepository.findByCurso(cursoFuncionario);
            // Admin pode ver todos os alunos
            case ADMIN:
                return alunoRepository.findAll();
            default:
                throw new AlunoNaoEncontradoException("Usuário não tem permissão para listar alunos por curso atual");
        }

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

    protected boolean existePorCuso(Long idCurso) {
        return cursoRepository.existsById(idCurso);
    }

    private boolean existePorEmail(String email) {
        return alunoRepository.findByEmail(email).isPresent();
    }

    private void validarCurso(Aluno aluno) {
        if (aluno.getCurso() == null || aluno.getCurso().getIdCurso() == null) {
            throw new IllegalArgumentException("Curso do aluno não pode ser nulo");
        }
        Curso curso = cursoRepository.findByIdCurso(aluno.getCurso().getIdCurso())
                .orElseThrow(() -> new CursoNaoEncontradoException("Curso não encontrado para o id: " + aluno.getCurso().getIdCurso()));
        aluno.setCurso(curso);
    }

    @Transactional
    public Aluno salvar(@Valid @NotNull(message = "O aluno não pode ser nulo") Aluno aluno) {
        usuarioService.prepararParaSalvar(aluno);
        validarEmailEmUso(aluno.getEmail());
        validarMatriculaEmUso(aluno.getMatricula());
        validarCurso(aluno);
        return alunoRepository.save(aluno);
    }

    @Transactional
    public Aluno atualizar(@Valid @NotNull(message = "O aluno não pode ser nulo") Aluno aluno) {
        Aluno alunoAtual = buscarPorIdOuFalhar(aluno.getIdUsuario());
        if (!alunoAtual.getEmail().equalsIgnoreCase(aluno.getEmail())) {
            validarEmailEmUso(aluno.getEmail());
        }
        if (!alunoAtual.getMatricula().equalsIgnoreCase(aluno.getMatricula())) {
            validarMatriculaEmUso(aluno.getMatricula());
        }
        validarCurso(aluno);
        return alunoRepository.save(aluno);
    }

    @Transactional
    public void excluir(
            @Valid @NotNull(message = "O ID do aluno não pode ser nulo")
            @Positive(message = "O ID deve ser um número positivo") Long idAluno) {
        Aluno aluno = buscarPorIdOuFalhar(idAluno);
        validarAlunoEmUsoTicket(aluno);
        alunoRepository.delete(aluno);
    }

    private void validarAlunoEmUsoTicket(Aluno aluno) {
        if (ticketRepository.existsByAluno(aluno)) {
            throw new AlunoNaoPodeSerExcluidoException(aluno.getIdUsuario());
        }
    }

    private void validarMatriculaEmUso(String matricula) {
        if (alunoRepository.findByMatricula(matricula).isPresent()) {
            throw new AlunoMatriculaEmUsoException(matricula);
        }
    }

    private void validarEmailEmUso(String email) {
        if (alunoRepository.findByEmail(email).isPresent()) {
            throw new AlunoEmailEmUsoException(email);
        }
    }


}