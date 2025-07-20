package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

class CategoriaTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    static class DummyCategoria extends Categoria {
        DummyCategoria(Long idCategoria, String nome) {
            super();
            setIdCategoria(idCategoria);
            setNome(nome);
        }
    }

    @Test
    void equalsEHashCode_mesmoId_deveSerIguais() {
        DummyCategoria c1 = new DummyCategoria(1L, "Cat1");
        DummyCategoria c2 = new DummyCategoria(1L, "Cat2");
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void equals_diferentesIds_naoDevemSerIguais() {
        Categoria c1 = new Categoria(1L, "Cat1");
        Categoria c2 = new Categoria(2L, "Cat1");
        assertNotEquals(c1, c2);
    }

    @Test
    void categoriaValida_semViolacoes() {
        Categoria categoria = new Categoria(1L, "Categoria");
        Set<ConstraintViolation<Categoria>> violacoes = validator.validate(categoria);
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void nomeEmBranco_deveGerarViolacaoNotBlank() {
        Categoria categoria = new Categoria(1L, "   ");
        Set<ConstraintViolation<Categoria>> violacoes = validator.validate(categoria);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeNulo_deveGerarViolacaoNotBlank() {
        Categoria categoria = new Categoria(1L, null);
        Set<ConstraintViolation<Categoria>> violacoes = validator.validate(categoria);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeMuitoLongo_deveGerarViolacaoSize() {
        String muitoLongo = "a".repeat(101);
        Categoria categoria = new Categoria(1L, muitoLongo);
        Set<ConstraintViolation<Categoria>> violacoes = validator.validate(categoria);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }
}
