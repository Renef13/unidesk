package br.ufma.glp.unidesk.backend.api.v1.dto.input;

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
@EqualsAndHashCode
public class MensagemCadastroInput {

    @NotBlank(message = "O conteúdo da mensagem é obrigatório")
    @Size(max = 1000, message = "O conteúdo não pode ter mais de 1000 caracteres")
    private String conteudo;

    @NotNull(message = "O ID do ticket é obrigatório")
    private Long idTicket;

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long idUsuario;
}