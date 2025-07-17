package br.ufma.glp.unidesk.backend.domain.model;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;



class UsuarioTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyUsuario extends Usuario {
        DummyUsuario(Long idUsuario, String nome, String email, String senha) {
            super(idUsuario, nome, email, senha);
        }
    }

    @Test
    void equalsEHashCode_mesmoId_deveSerIguais() {
        DummyUsuario u1 = new DummyUsuario(1L, "teste", "teste@discente.ufma.br", "pass1");
        DummyUsuario u2 = new DummyUsuario(1L, "teste2",   "teste2@discente.ufma.br",   "pass2");
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void equalsEHashCode_idsDiferentes_naoDevemSerIguais() {
        DummyUsuario u1 = new DummyUsuario(1L, "teste", "teste@discente.ufma.br", "pass1");
        DummyUsuario u2 = new DummyUsuario(2L, "teste", "teste@discente.ufma.br", "pass1");
        assertNotEquals(u1, u2);
    }

    @Test
    void usuarioValido_semViolacoes() {
        DummyUsuario user = new DummyUsuario(1L, "teste3", "teste3@discente.ufma.br", "senha123");
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(user);
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void nomeEmBranco_deveGerarViolacaoNotBlank() {
        DummyUsuario user = new DummyUsuario(1L, "   ", "teste3@discente.ufma.br", "senha123");
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(user);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream()
            .anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeNulo_deveGerarViolacaoNotBlank() {
        DummyUsuario user = new DummyUsuario(1L, null, "teste3@discente.ufma.br", "senha123");
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(user);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream()
            .anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void emailInvalido_deveGerarViolacaoEmail() {
        DummyUsuario user = new DummyUsuario(1L, "teste3", "email-invalido", "senha123");
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(user);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream()
            .anyMatch(v -> "email".equals(v.getPropertyPath().toString())));
    }

    @Test
    void emailNulo_deveGerarViolacaoNotBlank() {
        DummyUsuario user = new DummyUsuario(1L, "teste3", null, "senha123");
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(user);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream()
            .anyMatch(v -> "email".equals(v.getPropertyPath().toString())));
    }

    @Test
    void senhaEmBranco_deveGerarViolacaoNotBlank() {
        DummyUsuario user = new DummyUsuario(1L, "teste3", "teste3@discente.ufma.br", "");
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(user);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream()
            .anyMatch(v -> "senha".equals(v.getPropertyPath().toString())));
    }

    @Test
    void senhaNula_deveGerarViolacaoNotBlank() {
        DummyUsuario user = new DummyUsuario(1L, "teste3", "teste3@discente.ufma.br", null);
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(user);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream()
            .anyMatch(v -> "senha".equals(v.getPropertyPath().toString())));
    }

    // @Test
    // void builderDeUsuario_deveConfigurarCamposCorretamente() {
    //     DummyUsuario user = DummyUsuario.builder()
    //         .idUsuario(10L)
    //         .nome("Builder")
    //         .email("builder@discente.ufma.br")
    //         .senha("builder123")
    //         .build();

    //     assertEquals(10L, user.getIdUsuario());
    //     assertEquals("Builder", user.getNome());
    //     assertEquals("builder@discente.ufma.br", user.getEmail());
    //     assertEquals("builder123", user.getSenha());

    //     Set<ConstraintViolation<Usuario>> violacoes = validator.validate(user);
    //     assertTrue(violacoes.isEmpty());
    // }
}