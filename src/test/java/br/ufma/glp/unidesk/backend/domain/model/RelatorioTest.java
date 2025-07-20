package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RelatorioTest {
    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyRelatorio extends Relatorio {
        DummyRelatorio(Long id, String conteudo, LocalDate data, String tipoRelatorio) {
            super();
            setIdRelatorio(id);
            setConteudo(conteudo);
            setData(data);
            setTipoRelatorio(tipoRelatorio);
        }
    }

    @Test
    void equalsEHashCode_mesmoObjeto_deveSerIguais() {
        LocalDate hoje = LocalDate.of(2025, 7, 16);
        DummyRelatorio r1 = new DummyRelatorio(1L, "Conteudo", hoje, "TipoA");
        DummyRelatorio r2 = new DummyRelatorio(1L, "Conteudo", hoje, "TipoA");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equalsIdDiferente_naoDevemSerIguais() {
        LocalDate hoje = LocalDate.now();
        DummyRelatorio r1 = new DummyRelatorio(1L, "Conteudo", hoje, "TipoA");
        DummyRelatorio r2 = new DummyRelatorio(2L, "Conteudo", hoje, "TipoA");
        assertNotEquals(r1, r2);
    }

    @Test
    void equalsConteudoDiferente_naoDevemSerIguais() {
        LocalDate hoje = LocalDate.now();
        DummyRelatorio r1 = new DummyRelatorio(1L, "Conteudo1", hoje, "TipoA");
        DummyRelatorio r2 = new DummyRelatorio(1L, "Conteudo2", hoje, "TipoA");
        assertNotEquals(r1, r2);
    }

    @Test
    void equalsDataDiferente_naoDevemSerIguais() {
        DummyRelatorio r1 = new DummyRelatorio(1L, "Conteudo", LocalDate.of(2025, 7, 16), "TipoA");
        DummyRelatorio r2 = new DummyRelatorio(1L, "Conteudo", LocalDate.of(2025, 7, 17), "TipoA");
        assertNotEquals(r1, r2);
    }

    @Test
    void equalsTipoRelatorioDiferente_naoDevemSerIguais() {
        LocalDate hoje = LocalDate.now();
        DummyRelatorio r1 = new DummyRelatorio(1L, "Conteudo", hoje, "TipoA");
        DummyRelatorio r2 = new DummyRelatorio(1L, "Conteudo", hoje, "TipoB");
        assertNotEquals(r1, r2);
    }

    @Test
    void validacao_semViolacoes() {
        DummyRelatorio r = new DummyRelatorio(1L, "Conteudo", LocalDate.now(), "Tipo");
        Set<ConstraintViolation<Relatorio>> violacoes = validator.validate(r);
        assertTrue(violacoes.isEmpty());
    }
}
