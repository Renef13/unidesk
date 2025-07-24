package br.ufma.glp.unidesk.backend.domain.model;

import lombok.Getter;

@Getter
public enum UsuarioRole {
    ADMIN("admin"),
    COORDENADOR("coordenador"),
    FUNCIONARIO_COORDENACAO("funcionario_coordenacao"),
    ALUNO("aluno");

    private final String role;

    UsuarioRole(String role) {
        this.role = role;
    }

}
