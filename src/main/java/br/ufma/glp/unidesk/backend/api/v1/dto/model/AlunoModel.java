package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoModel {

    private Long idUsuario;
    
    @NotBlank(message = "O nome do aluno não pode ser vazio ou nulo")
    private String nome;
    
    @NotBlank(message = "O email do aluno não pode ser vazio ou nulo")
    @Email(message = "O email deve ser válido")
    private String email;
    
    @NotBlank(message = "Matrícula não pode ser vazia ou conter apenas espaços em branco")
    @Size(max = 20, message = "Matrícula não pode ter mais de 20 caracteres")
    private String matricula;
    
    @NotNull(message = "Curso não pode ser nulo")
    private CursoModel curso;
}