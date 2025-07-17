package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

class MensagemTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    static class DummyMensagem extends Mensagem {
        DummyMensagem(Long id, String conteudo, Usuario usuario, Ticket ticket) {
            super();
            setIdMensagem(id);
            setConteudo(conteudo);
            setUsuario(usuario);
            setTicket(ticket);
        }
    }

    static class DummyUsuario extends Usuario {
        DummyUsuario(Long id, String nome, String email, String senha) {
            super();
            setIdUsuario(id);
            setNome(nome);
            setEmail(email);
            setSenha(senha);
        }
    }

    static class DummyTicket extends Ticket {
        DummyTicket(Long id) {
            super();
            setIdTicket(id);
        }
    }

    @Test
    void equalsEHashCode_mesmoObjeto_deveSerIguais() {
        DummyUsuario u = new DummyUsuario(1L, "nome", "email@ufma.br", "senha");
        DummyTicket t = new DummyTicket(1L);
        DummyMensagem m1 = new DummyMensagem(1L, "msg", u, t);
        DummyMensagem m2 = new DummyMensagem(1L, "msg", u, t);
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void equals_idDiferente_naoDevemSerIguais() {
        DummyUsuario u = new DummyUsuario(1L, "nome", "email@ufma.br", "senha");
        DummyTicket t = new DummyTicket(1L);
        DummyMensagem m1 = new DummyMensagem(1L, "msg", u, t);
        DummyMensagem m2 = new DummyMensagem(2L, "msg", u, t);
        assertNotEquals(m1, m2);
    }

    @Test
    void equals_conteudoDiferente_naoDevemSerIguais() {
        DummyUsuario u = new DummyUsuario(1L, "nome", "email@ufma.br", "senha");
        DummyTicket t = new DummyTicket(1L);
        DummyMensagem m1 = new DummyMensagem(1L, "msg1", u, t);
        DummyMensagem m2 = new DummyMensagem(1L, "msg2", u, t);
        assertNotEquals(m1, m2);
    }

    @Test
    void equals_usuarioDiferente_naoDevemSerIguais() {
        DummyUsuario u1 = new DummyUsuario(1L, "nome1", "e1@ufma.br", "senha");
        DummyUsuario u2 = new DummyUsuario(2L, "nome2", "e2@ufma.br", "senha");
        DummyTicket t = new DummyTicket(1L);
        DummyMensagem m1 = new DummyMensagem(1L, "msg", u1, t);
        DummyMensagem m2 = new DummyMensagem(1L, "msg", u2, t);
        assertNotEquals(m1, m2);
    }

    @Test
    void equals_ticketDiferente_naoDevemSerIguais() {
        DummyUsuario u = new DummyUsuario(1L, "nome", "email@ufma.br", "senha");
        DummyTicket t1 = new DummyTicket(1L);
        DummyTicket t2 = new DummyTicket(2L);
        DummyMensagem m1 = new DummyMensagem(1L, "msg", u, t1);
        DummyMensagem m2 = new DummyMensagem(1L, "msg", u, t2);
        assertNotEquals(m1, m2);
    }

    @Test
    void validacao_semViolacoes() {
        DummyMensagem m = new DummyMensagem(1L, "conteudo", null, null);
        Set<ConstraintViolation<Mensagem>> violacoes = validator.validate(m);
        assertTrue(violacoes.isEmpty());
    }
}
