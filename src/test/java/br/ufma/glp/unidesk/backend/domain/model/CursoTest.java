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
        DummyCurso(Long idCurso, String nome, String campus, Coordenacao coordenacao) {
            super();
            setIdCurso(idCurso);
            setNome(nome);
            setCampus(campus);
            setCoordenacao(coordenacao);
        }
    }

    @Test
    void equalsEHashCode_mesmoId_deveSerIguais() {
        Coordenacao coordenacao = new Coordenacao(); // Criar instância de Coordenacao
        DummyCurso c1 = new DummyCurso(1L, "Curso1", "Campus1", coordenacao);
        DummyCurso c2 = new DummyCurso(1L, "Curso2", "Campus2", coordenacao);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void equals_diferentesIds_naoDevemSerIguais() {
        Coordenacao coordenacao = new Coordenacao(); // Criar instância de Coordenacao
        Curso c1 = new Curso(1L, "Curso1", "Campus1", coordenacao);
        Curso c2 = new Curso(2L, "Curso1", "Campus1", coordenacao);
        assertNotEquals(c1, c2);
    }

    @Test
    void cursoValido_semViolacoes() {
        Coordenacao coordenacao = new Coordenacao(); // Criar instância de Coordenacao
        Curso curso = new Curso(1L, "Curso", "Campus", coordenacao);
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void nomeEmBranco_deveGerarViolacaoNotBlank() {
        Coordenacao coordenacao = new Coordenacao(); // Criar instância de Coordenacao
        Curso curso = new Curso(1L, "   ", "Campus", coordenacao);
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeNulo_deveGerarViolacaoNotBlank() {
        Coordenacao coordenacao = new Coordenacao(); // Criar instância de Coordenacao
        Curso curso = new Curso(1L, null, "Campus", coordenacao);
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void campusEmBranco_deveGerarViolacaoNotBlank() {
        Coordenacao coordenacao = new Coordenacao(); // Criar instância de Coordenacao
        Curso curso = new Curso(1L, "Curso", "   ", coordenacao);
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "campus".equals(v.getPropertyPath().toString())));
    }

    @Test
    void campusNulo_deveGerarViolacaoNotBlank() {
        Coordenacao coordenacao = new Coordenacao(); // Criar instância de Coordenacao
        Curso curso = new Curso(1L, "Curso", null, coordenacao);
        Set<ConstraintViolation<Curso>> violacoes = validator.validate(curso);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "campus".equals(v.getPropertyPath().toString())));
    }
}
