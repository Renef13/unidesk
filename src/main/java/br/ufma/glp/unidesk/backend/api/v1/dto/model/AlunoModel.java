package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import br.ufma.glp.unidesk.backend.domain.model.UsuarioRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoModel {

    private Long idUsuario;

    private String usuario;

    private String nome;
    
    private String email;
    
    private String matricula;
    
    private CursoModel curso;

    private UsuarioRole role;
}