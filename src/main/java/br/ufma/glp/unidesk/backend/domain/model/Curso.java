package br.ufma.glp.unidesk.backend.domain.model;


import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Table;


@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_curso", nullable = false, unique = true)
    private Long idCurso;

    @Column(nullable = false, unique = true, name = "nome", length = 100)
    @NotBlank(message = "O nome do curso não pode ser vazio ou nulo")
    private String nome;

    @Column(name = "campus", nullable = false, length = 100)
    @NotBlank(message = "O campus do curso não pode ser vazio ou nulo")
    private String campus;
}
