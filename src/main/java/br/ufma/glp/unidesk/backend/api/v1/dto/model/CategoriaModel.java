package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaModel {

    private Long idCategoria;
    
    @NotBlank(message = "O nome da categoria não pode ser vazio")
    @Size(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres")
    private String nome;
}