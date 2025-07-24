package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import br.ufma.glp.unidesk.backend.domain.model.UsuarioRole;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
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
public class FuncionarioCoordenacaoCadastroInput {

    @NotBlank(message = "O nome do funcionário é obrigatório")
    @Size(max = 100, message = "O nome não pode ter mais de 100 caracteres")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Size(max = 100, message = "O email não pode ter mais de 100 caracteres")
    private String email;

    @NotBlank(message = "O nome de usuário não pode ser nula ou vazia, e deve ser único")
    @Column(nullable = false, unique = true)
    private String usuario;

    @NotBlank(message = "A senha é obrigatória")
    @Size(max = 100, message = "A senha não pode ter mais de 100 caracteres")
    private String senha;

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(max = 20, message = "Matrícula não pode ter mais de 20 caracteres")
    private String matricula;

    private UsuarioRole role;

    @NotNull(message = "O ID da coordenação é obrigatório")
    private Long idCoordenacao;
}