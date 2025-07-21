package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoModel {

    private Long idCurso;
    
    @NotBlank(message = "O nome do curso não pode ser vazio ou nulo")
    private String nome;
    
    @NotBlank(message = "O campus do curso não pode ser vazio ou nulo")
    private String campus;
}