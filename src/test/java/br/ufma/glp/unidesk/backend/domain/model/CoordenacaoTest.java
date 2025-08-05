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

    @Test
    void equalsEHashCode_mesmoId_deveSerIguais() {
        Curso curso1 = new Curso();
        Curso curso2 = new Curso();
        Coordenacao coord1 = new Coordenacao();
        Coordenacao coord2 = new Coordenacao();

        curso1.setIdCurso(1L);
        curso1.setNome("Curso1");
        curso1.setCampus("Campus1");

        curso2.setIdCurso(2L);
        curso2.setNome("Curso2");
        curso2.setCampus("Campus2");

        coord1.setIdCoordenacao(1L);
        coord1.setNome("Coord1");
        coord1.setCurso(curso1);

        coord2.setIdCoordenacao(1L);
        coord2.setNome("Coord2");
        coord2.setCurso(curso2);

        curso1.setCoordenacao(coord1);
        curso2.setCoordenacao(coord2);

        assertEquals(coord1, coord2);
        assertEquals(coord1.hashCode(), coord2.hashCode());
    }

    @Test
    void equalsEHashCode_idsDiferentes_naoDevemSerIguais() {
        Curso curso = new Curso();
        Coordenacao coord1 = new Coordenacao();
        Coordenacao coord2 = new Coordenacao();

        curso.setIdCurso(1L);
        curso.setNome("Curso1");
        curso.setCampus("Campus1");

        coord1.setIdCoordenacao(1L);
        coord1.setNome("Coord1");
        coord1.setCurso(curso);

        coord2.setIdCoordenacao(2L);
        coord2.setNome("Coord1");
        coord2.setCurso(curso);

        curso.setCoordenacao(coord1);

        assertNotEquals(coord1, coord2);
    }

    @Test
    void coordenacaoValida_semViolacoes() {
        Curso curso = new Curso();
        Coordenacao coord = new Coordenacao();

        curso.setIdCurso(1L);
        curso.setNome("Curso1");
        curso.setCampus("Campus1");

        coord.setIdCoordenacao(1L);
        coord.setNome("Coord1");
        coord.setCurso(curso);

        curso.setCoordenacao(coord);

        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void nomeEmBranco_deveGerarViolacaoNotBlank() {
        Curso curso = new Curso();
        Coordenacao coord = new Coordenacao();

        curso.setIdCurso(1L);
        curso.setNome("Curso1");
        curso.setCampus("Campus1");

        coord.setIdCoordenacao(1L);
        coord.setNome("   ");
        coord.setCurso(curso);

        curso.setCoordenacao(coord);

        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeNulo_deveGerarViolacaoNotBlank() {
        Curso curso = new Curso();
        Coordenacao coord = new Coordenacao();

        curso.setIdCurso(1L);
        curso.setNome("Curso1");
        curso.setCampus("Campus1");

        coord.setIdCoordenacao(1L);
        coord.setNome(null);
        coord.setCurso(curso);

        curso.setCoordenacao(coord);

        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void nomeMaiorQue100Caracteres_deveGerarViolacaoSize() {
        Curso curso = new Curso();
        Coordenacao coord = new Coordenacao();

        curso.setIdCurso(1L);
        curso.setNome("Curso1");
        curso.setCampus("Campus1");

        String nomeGrande = "a".repeat(101);
        coord.setIdCoordenacao(1L);
        coord.setNome(nomeGrande);
        coord.setCurso(curso);

        curso.setCoordenacao(coord);

        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "nome".equals(v.getPropertyPath().toString())));
    }

    @Test
    void cursoNulo_deveGerarViolacaoNotNull() {
        Coordenacao coord = new Coordenacao();

        coord.setIdCoordenacao(1L);
        coord.setNome("Coord1");
        coord.setCurso(null);

        Set<ConstraintViolation<Coordenacao>> violacoes = validator.validate(coord);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "curso".equals(v.getPropertyPath().toString())));
    }
}
