package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordenadorEdicaoInput {

    private Long idUsuario;

    private String nome;

    private String email;

    private String usuario;

    private String senha;

    private String matricula;

    private Long idCoordenacao;

    private Boolean ativo;
}