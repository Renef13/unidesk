package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Table(name = "alunos")
public class Aluno extends Usuario{

    @Column(name = "matricula", unique = true)
    @Size(max = 20, message = "Matrícula não pode ter mais de 20 caracteres")
    @NotBlank(message = "Matrícula não pode ser vazia ou conter apenas espaços em branco")
    private String matricula;

    @ManyToOne
    @NotNull(message = "Curso não pode ser nulo")
    @JoinColumn(name = "id_curso")
    private Curso curso;

    public Aluno(Long idUsuario, String nome, String email, String senha, String matricula, Curso curso) {
    }
}
