package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseConhecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArtigo;

    private String titulo;
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
}
