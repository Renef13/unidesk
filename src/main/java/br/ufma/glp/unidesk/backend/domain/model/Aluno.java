package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Table(name = "alunos")
public class Aluno extends Usuario{

    @Column(name = "matricula", unique = true)
    @NotBlank(message = "Matrícula não pode ser vazia ou conter apenas espaços em branco")
    private String matricula;

    @ManyToOne
    @NotNull(message = "Curso não pode ser nulo")
    @JoinColumn(name = "id_curso")
    private Curso curso;

}
