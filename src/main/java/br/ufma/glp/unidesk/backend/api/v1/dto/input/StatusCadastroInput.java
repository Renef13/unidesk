package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StatusCadastroInput {

    @NotBlank(message = "O nome do status é obrigatório")
    @Size(max = 50, message = "O nome do status deve ter no máximo 50 caracteres")
    private String nome;
}