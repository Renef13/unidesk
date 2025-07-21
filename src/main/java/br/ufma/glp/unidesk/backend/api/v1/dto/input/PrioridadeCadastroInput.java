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
public class PrioridadeCadastroInput {

    @NotBlank(message = "O nível de prioridade não pode ser vazio")
    @Size(max = 20, message = "O nível de prioridade deve ter no máximo 20 caracteres")
    private String nivel;
}