package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthInput {
    @NotBlank(message = "O nome de usuário é obrigatório")
    private String usuario;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}
