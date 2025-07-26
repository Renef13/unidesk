package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordenacaoCadastroInput {

    @NotBlank(message = "O nome da coordenação é obrigatório")
    @Size(max = 100, message = "O nome da coordenação não pode ter mais de 100 caracteres")
    private String nome;

    @NotNull(message = "O ID do curso é obrigatório")
    private Long idCurso;
}