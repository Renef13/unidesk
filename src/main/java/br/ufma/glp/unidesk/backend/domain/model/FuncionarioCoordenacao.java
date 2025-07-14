package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioCoordenacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFuncCoordenacao;

    private String matricula;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
