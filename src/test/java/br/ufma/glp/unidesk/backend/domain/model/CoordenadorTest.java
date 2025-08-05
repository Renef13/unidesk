package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CoordenadorTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyCoordenador extends Coordenador {
        DummyCoordenador(Long idUsuario, String nome, String email, String senha,
                         Boolean ativo, String matricula, Coordenacao coordenacao,UsuarioRole role, String usuario) {
            super();
            setIdUsuario(idUsuario);
            setNome(nome);
            setEmail(email);
            setSenha(senha);
            setAtivo(ativo);
            setMatricula(matricula);
            setCoordenacao(coordenacao);
            setRole(role);
            setUsuario(usuario);
        }
    }

    @Test
    void equalsEHashCode_mesmoObjeto_deveSerIguais() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coordenação Teste");

        Curso curso = new Curso(1L, "Cur", "Campus", coordenacao);
        coordenacao.setCurso(curso);

        DummyCoordenador c1 = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                    true, "mat1", coordenacao, UsuarioRole.COORDENADOR, "usuario");
        DummyCoordenador c2 = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                    true, "mat1", coordenacao, UsuarioRole.COORDENADOR, "usuario");
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void equalsEHashCode_diferentesCampos_naoDevemSerIguais() {
        Coordenacao coord1 = new Coordenacao();
        Coordenacao coord2 = new Coordenacao();

        Curso curso1 = new Curso();
        curso1.setIdCurso(1L);
        curso1.setNome("Cur");
        curso1.setCampus("Campus");
        curso1.setCoordenacao(coord1);

        Curso curso2 = new Curso();
        curso2.setIdCurso(2L);
        curso2.setNome("Cur");
        curso2.setCampus("Campus");
        curso2.setCoordenacao(coord2);

        coord1.setIdCoordenacao(1L);
        coord1.setNome("Coord1");
        coord1.setCurso(curso1);

        coord2.setIdCoordenacao(2L);
        coord2.setNome("Coord2");
        coord2.setCurso(curso2);

        DummyCoordenador c1 = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                    true, "mat1", coord1, UsuarioRole.COORDENADOR, "usuario");
        DummyCoordenador c2 = new DummyCoordenador(2L, "nome", "email@ufma.br", "senha",
                                                    true, "mat2", coord2, UsuarioRole.COORDENADOR, "usuario");

        assertNotEquals(c1, c2);
    }

    @Test
    void coordenadorValido_semViolacoes() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coord");

        Curso curso = new Curso();
        curso.setIdCurso(1L);
        curso.setNome("Cur");
        curso.setCampus("Campus");
        curso.setCoordenacao(coordenacao);

        coordenacao.setCurso(curso);

        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, "mat1", coordenacao, UsuarioRole.COORDENADOR, "usuario");

        Set<ConstraintViolation<Coordenador>> violacoes = validator.validate(c);
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void matriculaEmBranco_deveGerarViolacaoNotBlank() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coord");

        Curso curso = new Curso();
        curso.setIdCurso(1L);
        curso.setNome("Cur");
        curso.setCampus("Campus");
        curso.setCoordenacao(coordenacao);

        coordenacao.setCurso(curso);

        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, "   ", coordenacao, UsuarioRole.COORDENADOR, "usuario");

        Set<ConstraintViolation<Coordenador>> violacoes = validator.validate(c);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void matriculaNula_deveGerarViolacaoNotBlank() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coord");

        Curso curso = new Curso();
        curso.setIdCurso(1L);
        curso.setNome("Cur");
        curso.setCampus("Campus");
        curso.setCoordenacao(coordenacao);

        coordenacao.setCurso(curso);

        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, null, coordenacao, UsuarioRole.COORDENADOR, "usuario");

        Set<ConstraintViolation<Coordenador>> violacoes = validator.validate(c);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void matriculaMuitoLonga_deveGerarViolacaoSize() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coord");

        Curso curso = new Curso();
        curso.setIdCurso(1L);
        curso.setNome("Cur");
        curso.setCampus("Campus");
        curso.setCoordenacao(coordenacao);

        coordenacao.setCurso(curso);

        String muitoLonga = "123456789012345678901";
        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, muitoLonga, coordenacao, UsuarioRole.COORDENADOR, "usuario");

        Set<ConstraintViolation<Coordenador>> violacoes = validator.validate(c);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void coordenacaoNula_deveGerarViolacaoNotNull() {
        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, "mat1", null, UsuarioRole.COORDENADOR, "usuario");

        Set<ConstraintViolation<Coordenador>> violacoes = validator.validate(c);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "coordenacao".equals(v.getPropertyPath().toString())));
    }
}
