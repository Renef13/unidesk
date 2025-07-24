package br.ufma.glp.unidesk.backend.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

public class FuncionarioCoordenacaoTest {

    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static class DummyFuncionario extends FuncionarioCoordenacao {
        DummyFuncionario(Long idUsuario, String nome, String email, String senha,
                        String matricula, Coordenacao coord,UsuarioRole role, String usuario) {
            super();
            setIdUsuario(idUsuario);
            setNome(nome);
            setEmail(email);
            setSenha(senha);
            setMatricula(matricula);
            setCoordenacao(coord);
            setRole(role);
            setUsuario(usuario);

        }
    }

    @Test
    void equalsEHashCode_mesmoIdMatriculaCoordenacao_igualdade() {
        Coordenacao coord1 = new Coordenacao(1L, "Coord", new Curso(1L, "Curso", "Campus"));
        DummyFuncionario f1 = new DummyFuncionario(1L, "Nome1", "e1@discente.ufma.br", "pass1",
                                                   "MATR1", coord1, UsuarioRole.ADMIN, "usuario1");
        DummyFuncionario f2 = new DummyFuncionario(1L, "Nome2", "e2@discente.ufma.br", "pass2",
                                                   "MATR1", coord1, UsuarioRole.ADMIN, "usuario2");
        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void equals_idsDiferentes_naoIguais() {
        Coordenacao coord = new Coordenacao(1L, "Coord", new Curso(1L, "Curso", "Campus"));
        DummyFuncionario f1 = new DummyFuncionario(1L, "Nome", "e@discente.ufma.br", "pass",
                                                   "MATR", coord, UsuarioRole.ADMIN, "usuario1");
        DummyFuncionario f2 = new DummyFuncionario(2L, "Nome", "e@discente.ufma.br", "pass",
                                                   "MATR", coord, UsuarioRole.ADMIN, "usuario2");
        assertNotEquals(f1, f2);
    }

    @Test
    void funcionarioValido_semViolacoes() {
        Coordenacao coord = new Coordenacao(1L, "Coord", new Curso(1L, "Curso", "Campus"));
        DummyFuncionario f = new DummyFuncionario(1L, "Nome", "teste@discente.ufma.br", "senha",
                                                  "MATR", coord, UsuarioRole.ADMIN, "usuario");
        Set<ConstraintViolation<FuncionarioCoordenacao>> violations = validator.validate(f);
        assertTrue(violations.isEmpty());
    }

    @Test
    void matriculaEmBranco_violaNotBlank() {
        Coordenacao coord = new Coordenacao(1L, "Coord", new Curso(1L, "Curso", "Campus"));
        DummyFuncionario f = new DummyFuncionario(1L, "Nome", "teste@discente.ufma.br", "senha",
                                                  "   ", coord, UsuarioRole.ADMIN, "usuario");
        Set<ConstraintViolation<FuncionarioCoordenacao>> violations = validator.validate(f);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void matriculaMuitoLonga_violaSize() {
        String longo = "a".repeat(21);
        Coordenacao coord = new Coordenacao(1L, "Coord", new Curso(1L, "Curso", "Campus"));
        DummyFuncionario f = new DummyFuncionario(1L, "Nome", "teste@discente.ufma.br", "senha",
                                                  longo, coord, UsuarioRole.ADMIN, "usuario");
        Set<ConstraintViolation<FuncionarioCoordenacao>> violations = validator.validate(f);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "matricula".equals(v.getPropertyPath().toString())));
    }

    @Test
    void coordenacaoNula_violaNotNull() {
        DummyFuncionario f = new DummyFuncionario(1L, "Nome", "teste@discente.ufma.br", "senha",
                                                  "MATR", null, UsuarioRole.ADMIN, "usuario");
        Set<ConstraintViolation<FuncionarioCoordenacao>> violations = validator.validate(f);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "coordenacao".equals(v.getPropertyPath().toString())));
    }
}
