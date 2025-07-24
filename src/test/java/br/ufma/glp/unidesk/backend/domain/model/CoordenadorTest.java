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
        Coordenacao coord = new Coordenacao(1L, "Coord", new Curso(1L, "Cur", "Campus"));
        DummyCoordenador c1 = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                    true, "mat1", coord, UsuarioRole.COORDENADOR, "usuario");
        DummyCoordenador c2 = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                    true, "mat1", coord, UsuarioRole.COORDENADOR, "usuario");
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void equalsEHashCode_diferentesCampos_naoDevemSerIguais() {
        Coordenacao coord1 = new Coordenacao(1L, "Coord1", new Curso(1L, "Cur", "Campus"));
        Coordenacao coord2 = new Coordenacao(2L, "Coord2", new Curso(1L, "Cur", "Campus"));
        DummyCoordenador c1 = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                    true, "mat1", coord1, UsuarioRole.COORDENADOR, "usuario");
        DummyCoordenador c2 = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                    true, "mat1", coord2, UsuarioRole.COORDENADOR, "usuario");
        assertNotEquals(c1, c2);
    }

    @Test
    void coordenadorValido_semViolacoes() {
        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, "mat1", new Coordenacao(1L, "Coord",
                                                   new Curso(1L, "Cur", "Campus")),UsuarioRole.COORDENADOR,"usuario");
        Set<ConstraintViolation<Coordenador>> violacoes = validator.validate(c);
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void matriculaEmBranco_deveGerarViolacaoNotBlank() {
        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, "   ", new Coordenacao(1L, "Coord",
                                                   new Curso(1L, "Cur", "Campus")),UsuarioRole.COORDENADOR, "usuario");
        Set<ConstraintViolation<Coordenador>> violacoes = validator.validate(c);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void matriculaNula_deveGerarViolacaoNotBlank() {
        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, null, new Coordenacao(1L, "Coord",
                                                   new Curso(1L, "Cur", "Campus")), UsuarioRole.COORDENADOR, "usuario");
        Set<ConstraintViolation<Coordenador>> violacoes = validator.validate(c);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void matriculaMuitoLonga_deveGerarViolacaoSize() {
        String muitoLonga = "123456789012345678901";
        DummyCoordenador c = new DummyCoordenador(1L, "nome", "email@ufma.br", "senha",
                                                   true, muitoLonga, new Coordenacao(1L, "Coord",
                                                   new Curso(1L, "Cur", "Campus")),UsuarioRole.COORDENADOR, "usuario");
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
