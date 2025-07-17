package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class StatusTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyStatus extends Status {
        DummyStatus(Long id, String nome) {
            super();
            setIdStatus(id);
            setNome(nome);
        }
    }

    @Test
    void nomeEmBranco_deveGerarViolacaoNotBlank() {
        DummyStatus s = new DummyStatus(1L, "   ");
        Set<ConstraintViolation<Status>> violacoes = validator.validate(s);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeNulo_deveGerarViolacaoNotBlank() {
        DummyStatus s = new DummyStatus(1L, null);
        Set<ConstraintViolation<Status>> violacoes = validator.validate(s);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeMuitoLongo_deveGerarViolacaoSize() {
        String muitoLongo = "a".repeat(51);
        DummyStatus s = new DummyStatus(1L, muitoLongo);
        Set<ConstraintViolation<Status>> violacoes = validator.validate(s);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void statusValido_semViolacoes() {
        DummyStatus s = new DummyStatus(1L, "Ativo");
        Set<ConstraintViolation<Status>> violacoes = validator.validate(s);
        assertTrue(violacoes.isEmpty());
    }
}
