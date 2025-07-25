package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioCadastroInput {

    @NotBlank(message = "O conteúdo do relatório é obrigatório")
    @Size(max = 1000, message = "O conteúdo não pode ter mais de 1000 caracteres")
    private String conteudo;
    
    @NotNull(message = "A data do relatório é obrigatória")
    private LocalDate data;
    
    @NotBlank(message = "O tipo do relatório é obrigatório")
    @Size(max = 50, message = "O tipo não pode ter mais de 50 caracteres")
    private String tipoRelatorio;
}