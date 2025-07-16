package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long idCategoria;


    @Column(name = "nome", nullable = false, unique = true, length = 100)
    @NotBlank(message = "O nome da categoria não pode ser vazio")
    @Size(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres")
    private String nome;
}
