package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordenadorModel {

    private Long idUsuario;

    private String usuario;

    private String nome;

    private String email;

    private String matricula;

    private CoordenacaoModel coordenacao;
}