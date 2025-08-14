package br.ufma.glp.unidesk.backend.domain.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.ufma.glp.unidesk.backend.domain.model.Status;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.repository.TicketMovimentacaoRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class VerificaTickets {
    private final long SEGUNDO = 1000; 
    private final long MINUTO = SEGUNDO * 60; 
    private final long HORA = MINUTO * 60;

    private final TicketMovimentacaoRepository ticketMovimentacaoRepository;
    private final StatusService statusService;
    private final TicketRepository ticketRepository;
    @Scheduled(fixedDelay = HORA)
    public void verificarPorHoraTicketsSemTratamento() {
        // pegar a data atual e a data de dois dias atras
        Instant dataAtual = Instant.now();

        Instant doisDiasAtras = dataAtual.minus(2, ChronoUnit.DAYS);
        // pegar os tickets com movimentacao mais antiga que dois dias atras ()
        List<Ticket> ticketsNaoTratadosNoPrazo = ticketMovimentacaoRepository.findAll().stream().filter(t -> t.getDataMovimentacao() != null && t.getDataMovimentacao().isBefore(doisDiasAtras)).map(t -> t.getTicket()).distinct().toList();
        // para cada ticket desse nao tratado, mudar o status para fechado
        Status statusFechado = statusService.buscarPorNomeOuFalhar("Fechado");
        ticketsNaoTratadosNoPrazo.stream().forEach(t -> t.setStatus(statusFechado));
        ticketRepository.saveAll(ticketsNaoTratadosNoPrazo);
    }
}
