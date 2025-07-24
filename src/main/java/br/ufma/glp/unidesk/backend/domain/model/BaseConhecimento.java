package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseConhecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArtigo;

    @NotBlank(message = "O título do artigo não pode ser nulo ou vazio")
    private String titulo;

    @NotBlank(message = "O conteúdo do artigo não pode ser nulo ou vazio")
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    @NotNull(message = "A categoria não pode ser nula")
    private Categoria categoria;
}
