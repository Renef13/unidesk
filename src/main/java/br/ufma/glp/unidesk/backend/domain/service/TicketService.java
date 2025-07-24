package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.StatusNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.TicketNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.repository.StatusRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StorageService storageService;
    private final StatusRepository statusRepository;

    public Page<Ticket> listarTickets(@Valid @NotNull Usuario usuario, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        boolean isAdmin = usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"));
        if (isAdmin) {
            return ticketRepository.findAll(pageable);
        } else {
            return ticketRepository.findByAlunoIdUsuario(usuario.getIdUsuario(), pageable);
        }
    }

    @Transactional
    public Ticket novoTicket(@Valid @NotNull(message = "O Ticket nao pode ser nulo") Ticket ticket, MultipartFile file) throws Exception {
        if (file != null) {
            String idFile = storageService.uploadFile(file);
            ticket.setIdFile(idFile);
        }
        return ticketRepository.save(ticket);
    }

    @Transactional
    public String alterarStatusTicket(@Valid @NotNull Ticket ticket, @Valid @NotNull Status status) {
        ticket.setStatus(status);
        ticketRepository.save(ticket);
        return "Novo status do ticket: " + status.getNome();
    }

    @Transactional
    public Ticket fecharTicket(@Valid @NotNull Ticket ticket, @NotNull Long idStatus) {
        Status novoStatus = statusRepository.findById(idStatus)
                .orElseThrow(() -> new StatusNaoEncontradoException("Status não encontrado para o id: " + idStatus));
        ticket.setStatus(novoStatus);
        return ticketRepository.save(ticket);
    }

    public Ticket buscarTicketPorId(@Valid @NotNull(message = "O Id do ticket nao pode ser nulo") Long idTicket) {
        return ticketRepository.findById(idTicket)
                .orElseThrow(() -> new TicketNaoEncontradoException(idTicket));
    }

    public void filtrarPorCategoria() {
        //TODO: Implementação futura
    }

    public List<Ticket> buscarTicketsPorStatusEspecifico(@Valid @NotNull Usuario usuario, @Valid @NotNull Status status) {
        boolean isAdmin = usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"));
        Status statusTipo = statusRepository.findByIdStatus(status.getIdStatus())
                .orElseThrow(() -> new StatusNaoEncontradoException("Status com tipo " + status.getNome() + " nao encontrado"));
        if (isAdmin) {
            return ticketRepository.findByStatus(statusTipo);
        }
        return List.of();
    }

    public List<Ticket> buscarTicketsPorPeriodo(@NotNull Instant dataInicio, @NotNull Instant dataFim) {
        List<Ticket> ticketsNoPeriodo = ticketRepository.findByDataCriacaoBetween(dataInicio, dataFim);
        if (ticketsNoPeriodo.isEmpty()) {
            throw new TicketNaoEncontradoException("Nao encontrado tickets no periodo selecionado");
        }
        return ticketsNoPeriodo;
    }
}