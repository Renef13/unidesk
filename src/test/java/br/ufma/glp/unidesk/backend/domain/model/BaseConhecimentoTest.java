package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

class BaseConhecimentoTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    static class DummyArtigo extends BaseConhecimento {
        DummyArtigo(Long id, String titulo, String conteudo, Categoria categoria) {
            super();
            setIdArtigo(id);
            setTitulo(titulo);
            setConteudo(conteudo);
            setCategoria(categoria);
        }
    }

    @Test
    void equalsEHashCode_mesmoObjeto_deveSerIguais() {
        Categoria cat = new Categoria(1L, "Cat", "Camp");
        DummyArtigo a1 = new DummyArtigo(1L, "T1", "C1", cat);
        DummyArtigo a2 = new DummyArtigo(1L, "T1", "C1", cat);
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void equals_idDiferente_naoDevemSerIguais() {
        Categoria cat = new Categoria(1L, "Cat", "Camp");
        DummyArtigo a1 = new DummyArtigo(1L, "T1", "C1", cat);
        DummyArtigo a2 = new DummyArtigo(2L, "T1", "C1", cat);
        assertNotEquals(a1, a2);
    }

    @Test
    void equals_conteudoDiferente_naoDevemSerIguais() {
        Categoria cat = new Categoria(1L, "Cat", "Camp");
        DummyArtigo a1 = new DummyArtigo(1L, "T1", "C1", cat);
        DummyArtigo a2 = new DummyArtigo(1L, "T1", "C2", cat);
        assertNotEquals(a1, a2);
    }

    @Test
    void validacao_semViolacoes() {
        DummyArtigo art = new DummyArtigo(1L, "Titulo", "Conteudo", null);
        Set<ConstraintViolation<BaseConhecimento>> violacoes = validator.validate(art);
        assertTrue(violacoes.isEmpty());
    }
}
