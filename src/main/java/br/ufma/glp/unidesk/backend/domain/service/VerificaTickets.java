package br.ufma.glp.unidesk.backend.domain.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.ufma.glp.unidesk.backend.domain.model.Status;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.model.TicketMovimentacao;
import br.ufma.glp.unidesk.backend.domain.repository.TicketMovimentacaoRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Scheduled(fixedDelay = HORA)
    public void verificarPorHoraTicketsSemTratamento() {
        // pegar a data atual e a data de dois dias atras
        Instant dataAtual = Instant.now();

        Instant doisDiasAtras = dataAtual.minus(2, ChronoUnit.DAYS);
        Map<Ticket, TicketMovimentacao> ultimaMovimentacaoPorTicket = ticketMovimentacaoRepository
            .findAll().stream().filter(tm -> tm.getDataMovimentacao() != null)
                .collect(Collectors.groupingBy(
                    tm -> tm.getTicket(), Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparing(tm -> tm.getDataMovimentacao())), tm -> tm.get())
            ));

        // pegar os tickets com movimentacao mais antiga que dois dias atras ()
        List<Ticket> ticketsNaoTratadosNoPrazo = ultimaMovimentacaoPorTicket.values().stream().filter(tm -> tm.getDataMovimentacao().isBefore(doisDiasAtras)).map( tm -> tm.getTicket()).toList();
        for(Ticket t: ticketsNaoTratadosNoPrazo) {
            System.out.println(t.getDataAtualizacao());
        }
        // para cada ticket desse nao tratado, mudar o status para fechado
        Status statusFechado = statusService.buscarPorNomeOuFalhar("Fechado");
        ticketsNaoTratadosNoPrazo.stream().forEach(t -> t.setStatus(statusFechado));
        ticketRepository.saveAll(ticketsNaoTratadosNoPrazo);
    }
}
