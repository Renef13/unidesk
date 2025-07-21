package br.ufma.glp.unidesk.backend.domain.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idTicket;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Título não pode ser vazio")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    private String titulo;

    @Column(nullable = false)
    @NotBlank(message = "Descrição não pode ser vazia")
    private String descricao;
    
    @Column(name = "id_file")
    private String idFile;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_fechamento")
    private Instant dataFechamento;

    //TODO: analisar se não vale a pena usar esse atributo
    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private Instant dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_coordenacao", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_coordenacao"))
    @NotNull(message = "Coordenação é obrigatória")
    private Coordenacao coordenacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_func_coordenacao", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_func_coordenacao"))
    @NotNull(message = "Funcionário da coordenação é obrigatório")
    private FuncionarioCoordenacao funcionario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_aluno", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_aluno"))
    @NotNull(message = "Aluno é obrigatório")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_status", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_status"))
    @NotNull(message = "Status é obrigatório")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prioridade", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_prioridade"))
    @NotNull(message = "Prioridade é obrigatória")
    private Prioridade prioridade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_categoria"))
    @NotNull(message = "Categoria é obrigatória")
    private Categoria categoria;

    

    @AssertTrue(message = "Data de fechamento não pode ser anterior à data de criação")
    private boolean isFechaValida() {
        return dataFechamento == null
                || !dataFechamento.isBefore(dataCriacao);
    }
}
