package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.TicketMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketMovimentacaoRepository extends JpaRepository<TicketMovimentacao, Long> {
    List<TicketMovimentacao> findByTicketIdTicketOrderByDataMovimentacaoAsc(Long idTicket);
}
