package br.ufma.glp.unidesk.backend.domain.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "coordenacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "curso")
public class Coordenacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, unique = true)
    private Long idCoordenacao;

    @Column(name = "nome" ,nullable = false, unique = true)
    @NotBlank(message = "O nome da coordenação não pode ser vazio ou nulo")
    @Size(max = 100, message = "O nome da coordenação não pode ter mais de 100 caracteres")
    private String nome;

    @OneToOne
    @JoinColumn(name = "id_curso", nullable = false, unique = true)
    @NotNull(message = "A coordenação deve estar vinculada a um curso")
    private Curso curso;
}
