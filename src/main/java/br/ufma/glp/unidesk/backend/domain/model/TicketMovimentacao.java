package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "ticket_movimentacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ticket", nullable = false, foreignKey = @ForeignKey(name = "fk_mov_ticket"))
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_origem", nullable = false, foreignKey = @ForeignKey(name = "fk_mov_usuario_origem"))
    private Usuario usuarioOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_destino", foreignKey = @ForeignKey(name = "fk_mov_usuario_destino"))
    private Usuario usuarioDestino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @CreationTimestamp
    @Column(name = "data_movimentacao", nullable = false, updatable = false)
    private Instant dataMovimentacao;
}
