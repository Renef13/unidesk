package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class CoordenacaoTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyCoordenacao extends Coordenacao {
        DummyCoordenacao(Long idCoordenacao, String nome, Curso curso) {
            super(idCoordenacao, nome, curso);
        }
    }

    @Test
    void equalsEHashCode_mesmoId_deveSerIguais() {
        DummyCoordenacao c1 = new DummyCoordenacao(1L, "Coord1", new Curso(1L, "Curso1", "Campus1"));
        DummyCoordenacao c2 = new DummyCoordenacao(1L, "Coord2", new Curso(2L, "Curso2", "Campus2"));
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void equalsEHashCode_idsDiferentes_naoDevemSerIguais() {
        DummyCoordenacao c1 = new DummyCoordenacao(1L, "Coord1", new Curso(1L, "Curso1", "Campus1"));
        DummyCoordenacao c2 = new DummyCoordenacao(2L, "Coord1", new Curso(1L, "Curso1", "Campus1"));
        assertNotEquals(c1, c2);
    }

    @Test
    void coordenacaoValida_semViolacoes() {
        DummyCoordenacao coord = new DummyCoordenacao(1L, "Coord1", new Curso(1L, "Curso1", "Campus1"));
        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void nomeEmBranco_deveGerarViolacaoNotBlank() {
        DummyCoordenacao coord = new DummyCoordenacao(1L, "   ", new Curso(1L, "Curso1", "Campus1"));
        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeNulo_deveGerarViolacaoNotBlank() {
        DummyCoordenacao coord = new DummyCoordenacao(1L, null, new Curso(1L, "Curso1", "Campus1"));
        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeMaiorQue100Caracteres_deveGerarViolacaoSize() {
        String nomeGrande = "a".repeat(101);
        DummyCoordenacao coord = new DummyCoordenacao(1L, nomeGrande, new Curso(1L, "Curso1", "Campus1"));
        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void cursoNulo_deveGerarViolacaoNotNull() {
        DummyCoordenacao coord = new DummyCoordenacao(1L, "Coord1", null);
        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "curso".equals(v.getPropertyPath().toString())));
    }
}
