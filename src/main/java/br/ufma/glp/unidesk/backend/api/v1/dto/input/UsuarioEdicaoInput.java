package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import br.ufma.glp.unidesk.backend.domain.model.UsuarioRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEdicaoInput {

    private String nome;

    private String email;

    private String senha;

    UsuarioRole role;
}