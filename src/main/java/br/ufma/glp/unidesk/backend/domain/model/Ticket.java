package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    private String titulo;
    private String descricao;

    private LocalDate dataCriacao;
    private LocalDate dataFechamento;

    @ManyToOne
    @JoinColumn(name = "id_coordenacao")
    private Coordenacao coordenacao;

    @ManyToOne
    @JoinColumn(name = "id_func_coordenacao2")
    private FuncionarioCoordenacao funcionario;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "id_prioridade")
    private Prioridade prioridade;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
}
