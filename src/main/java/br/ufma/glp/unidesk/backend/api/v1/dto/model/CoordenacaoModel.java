package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordenacaoModel {

    private Long idCoordenacao;

    private String nome;

    @JsonIgnore
    private CursoModel curso;

}