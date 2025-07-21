package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseConhecimentoModel {

    private Long idArtigo;
    
    private String titulo;
    
    private String conteudo;
    
    private CategoriaModel categoria;
}