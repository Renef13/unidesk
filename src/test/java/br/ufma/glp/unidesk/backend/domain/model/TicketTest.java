package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TicketTest {
    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyTicket extends Ticket {
        DummyTicket(Long id) {
            super();
            setIdTicket(id);
        }
    }

    @Test
    void equalsEHashCode_mesmoId_deveSerIguais() {
        DummyTicket t1 = new DummyTicket(1L);
        DummyTicket t2 = new DummyTicket(1L);
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void equalsDiferentesIds_naoDevemSerIguais() {
        DummyTicket t1 = new DummyTicket(1L);
        DummyTicket t2 = new DummyTicket(2L);
        assertNotEquals(t1, t2);
    }

    @Test
    void tituloEmBranco_deveGerarViolacaoNotBlank() {
        Ticket t = new Ticket();
        t.setTitulo("   ");
        Set<ConstraintViolation<Ticket>> violacoes = validator.validate(t);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "titulo".equals(v.getPropertyPath().toString())));
    }

    @Test
    void tituloNulo_deveGerarViolacaoNotBlank() {
        Ticket t = new Ticket();
        t.setTitulo(null);
        Set<ConstraintViolation<Ticket>> violacoes = validator.validate(t);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "titulo".equals(v.getPropertyPath().toString())));
    }

    @Test
    void tituloMuitoLongo_deveGerarViolacaoSize() {
        String longo = "a".repeat(101);
        Ticket t = new Ticket();
        t.setTitulo(longo);
        Set<ConstraintViolation<Ticket>> violacoes = validator.validate(t);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "titulo".equals(v.getPropertyPath().toString())));
    }

    @Test
    void descricaoEmBranco_deveGerarViolacaoNotBlank() {
        Ticket t = new Ticket();
        t.setTitulo("Titulo");
        t.setDescricao("   ");
        Set<ConstraintViolation<Ticket>> violacoes = validator.validate(t);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "descricao".equals(v.getPropertyPath().toString())));
    }

    @Test
    void descricaoNula_deveGerarViolacaoNotBlank() {
        Ticket t = new Ticket();
        t.setTitulo("Titulo");
        t.setDescricao(null);
        Set<ConstraintViolation<Ticket>> violacoes = validator.validate(t);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "descricao".equals(v.getPropertyPath().toString())));
    }

    @Test
    void fechaInvalida_deveGerarViolacaoAssertTrue() {
        Ticket t = new Ticket();
        Instant criacao = Instant.now();
        t.setDataCriacao(criacao);
        t.setDataFechamento(criacao.minusSeconds(60));
        Set<ConstraintViolation<Ticket>> violacoes = validator.validate(t);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "fechaValida".equals(v.getPropertyPath().toString())));
    }

    @Test
    void validacao_semViolacoes() {
        Ticket t = new Ticket();
        Instant now = Instant.now();
        t.setTitulo("Titulo");
        t.setDescricao("Descricao");
        t.setDataCriacao(now);
        t.setDataAtualizacao(now);
        t.setDataFechamento(now.plusSeconds(60));
        t.setCoordenacao(new Coordenacao());
        t.setFuncionario(new FuncionarioCoordenacao());
        t.setAluno(new Aluno());
        t.setStatus(new Status());
        t.setPrioridade(new Prioridade());
        t.setCategoria(new Categoria());
        Set<ConstraintViolation<Ticket>> violacoes = validator.validate(t);
        assertTrue(violacoes.isEmpty());
    }
}
