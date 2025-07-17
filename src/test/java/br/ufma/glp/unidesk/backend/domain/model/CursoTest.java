package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class CursoTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    static class DummyCurso extends Curso {
        DummyCurso(Long idCurso, String nome, String campus) {
            super();
            setIdCurso(idCurso);
            setNome(nome);
            setCampus(campus);
        }
    }

    @Test
    void equalsEHashCode_mesmoId_deveSerIguais() {
        DummyCurso c1 = new DummyCurso(1L, "Curso1", "Campus1");
        DummyCurso c2 = new DummyCurso(1L, "Curso2", "Campus2");
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void equals_diferentesIds_naoDevemSerIguais() {
        Curso c1 = new Curso(1L, "Curso1", "Campus1");
        Curso c2 = new Curso(2L, "Curso1", "Campus1");
        assertNotEquals(c1, c2);
    }

    @Test
    void cursoValido_semViolacoes() {
        Curso curso = new Curso(1L, "Curso", "Campus");
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void nomeEmBranco_deveGerarViolacaoNotBlank() {
        Curso curso = new Curso(1L, "   ", "Campus");
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeNulo_deveGerarViolacaoNotBlank() {
        Curso curso = new Curso(1L, null, "Campus");
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void campusEmBranco_deveGerarViolacaoNotBlank() {
        Curso curso = new Curso(1L, "Curso", "   ");
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "campus".equals(v.getPropertyPath().toString())));
    }

    @Test
    void campusNulo_deveGerarViolacaoNotBlank() {
        Curso curso = new Curso(1L, "Curso", null);
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "campus".equals(v.getPropertyPath().toString())));
    }
}
