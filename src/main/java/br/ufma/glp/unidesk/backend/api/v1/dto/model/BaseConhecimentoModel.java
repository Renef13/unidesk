package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseConhecimentoModel {

    private Long idArtigo;
    
    @NotBlank(message = "O título do artigo não pode ser vazio ou nulo")
    private String titulo;
    
    @NotBlank(message = "O conteúdo do artigo não pode ser vazio ou nulo")
    private String conteudo;
    
    @NotNull(message = "A categoria não pode ser nula")
    private CategoriaModel categoria;
}