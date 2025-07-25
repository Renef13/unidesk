package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordenacaoEdicaoInput {

    private Long idCoordenacao;

    private String nome;

    private Long idCurso;
}