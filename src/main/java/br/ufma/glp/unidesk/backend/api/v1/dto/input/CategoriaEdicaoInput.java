package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class CategoriaEdicaoInput extends CategoriaCadastroInput {
    
    @EqualsAndHashCode.Include
    private Long idCategoria;
}