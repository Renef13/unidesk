package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordenacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCoordenacao;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;
}
