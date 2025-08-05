package br.ufma.glp.unidesk.backend.domain.model;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class AlunoTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyAluno extends Aluno {
        DummyAluno(Long idUsuario, String nome, String email, String senha, String matricula, Curso curso) {
            super();
            setIdUsuario(idUsuario);
            setNome(nome);
            setEmail(email);
            setSenha(senha);
            setMatricula(matricula);
            setCurso(curso);
        }
    }

    @Test
    void equalsEHashCode_mesmoObjeto_deveSerIguais() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coordenação Teste");

        Curso curso = new Curso(1L, "Cur", "Campus", coordenacao);
        coordenacao.setCurso(curso);

        DummyAluno a1 = new DummyAluno(1L, "nome", "teste@discente.ufma.br", "senha", "mat1", curso);
        DummyAluno a2 = new DummyAluno(1L, "nome", "teste@discente.ufma.br", "senha", "mat1", curso);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void matriculaEmBranco_deveGerarViolacaoNotBlank() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coordenação Teste");

        Curso curso = new Curso(1L, "Cur", "Campus", coordenacao);
        coordenacao.setCurso(curso);

        DummyAluno aluno = new DummyAluno(1L, "nome", "teste@discente.ufma.br", "senha", "   ", curso);
        Set<ConstraintViolation<Aluno>> violacoes = validator.validate(aluno);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void matriculaNula_deveGerarViolacaoNotBlank() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coordenação Teste");

        Curso curso = new Curso(1L, "Cur", "Campus", coordenacao);
        coordenacao.setCurso(curso);

        DummyAluno aluno = new DummyAluno(1L, "nome", "teste@discente.ufma.br", "senha", null, curso);
        Set<ConstraintViolation<Aluno>> violacoes = validator.validate(aluno);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void matriculaMuitoLonga_deveGerarViolacaoSize() {
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        coordenacao.setNome("Coordenação Teste");

        Curso curso = new Curso(1L, "Cur", "Campus", coordenacao);
        coordenacao.setCurso(curso);

        String muitoLonga = "123456789012345678901"; // 21 caracteres
        DummyAluno aluno = new DummyAluno(1L, "nome", "teste@discente.ufma.br", "senha", muitoLonga, curso);
        Set<ConstraintViolation<Aluno>> violacoes = validator.validate(aluno);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void cursoNulo_deveGerarViolacaoNotNull() {
        DummyAluno aluno = new DummyAluno(1L, "nome", "teste@discente.ufma.br", "senha", "mat1", null);
        Set<ConstraintViolation<Aluno>> violacoes = validator.validate(aluno);
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> "curso".equals(v.getPropertyPath().toString())));
    }
}