package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseConhecimentoEdicaoInput {

    private String titulo;

    private String conteudo;

    private Long idCategoria;
}