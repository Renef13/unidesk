package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PrioridadeTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyPrioridade extends Prioridade {
        DummyPrioridade(Long id, String nivel) {
            super();
            setIdPrioridade(id);
            setNivel(nivel);
        }
    }

    @Test
    void equalsEHashCode_mesmoObjeto_deveSerIguais() {
        DummyPrioridade p1 = new DummyPrioridade(1L, "Alta");
        DummyPrioridade p2 = new DummyPrioridade(1L, "Baixa");
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void equalsDiferentesIDs_naoDevemSerIguais() {
        DummyPrioridade p1 = new DummyPrioridade(1L, "Alta");
        DummyPrioridade p2 = new DummyPrioridade(2L, "Alta");
        assertNotEquals(p1, p2);
    }

    @Test
    void nivelEmBranco_deveGerarViolacaoNotBlank() {
        DummyPrioridade p = new DummyPrioridade(1L, "   ");
        Set<ConstraintViolation<Prioridade>> violacoes = validator.validate(p);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nivel".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nivelNulo_deveGerarViolacaoNotBlank() {
        DummyPrioridade p = new DummyPrioridade(1L, null);
        Set<ConstraintViolation<Prioridade>> violacoes = validator.validate(p);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nivel".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nivelMuitoLongo_deveGerarViolacaoSize() {
        String muitoLongo = "123456789012345678901"; //21 caracteres
        DummyPrioridade p = new DummyPrioridade(1L, muitoLongo);
        Set<ConstraintViolation<Prioridade>> violacoes = validator.validate(p);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nivel".equals(v.getPropertyPath().toString())));
    }
}
