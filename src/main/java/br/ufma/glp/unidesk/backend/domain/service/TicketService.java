package br.ufma.glp.unidesk.backend.domain.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufma.glp.unidesk.backend.domain.exception.StatusNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.TicketNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.repository.StatusRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class TicketService {

    private TicketRepository ticketRepository;
    private StorageService storageService;
    private StatusRepository statusRepository;

    public TicketService(TicketRepository ticketRepository, StorageService storageService, StatusRepository statusRepository) {
        this.ticketRepository = ticketRepository;
        this.storageService = storageService;
        this.statusRepository = statusRepository;
    }

    public Page<Ticket> listarTickets(Usuario usuario, int pageNumber, int pageSize) {
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

        ticketRepository.save(ticket);
        return ticket;
    }

    @Transactional
    public String alterarStatusTicket(Ticket ticket, Status status) {
        ticket.setStatus(status);
        return "Novo status do ticket: " + status.getNome();
    }

    @Transactional
    public Ticket fecharTicket(Ticket ticket, Long idStatus) {
        Status novoStatus = statusRepository.findById(idStatus).get();
        ticket.setStatus(novoStatus);

        return ticket;
    }

    public Ticket buscarTicketPorId(@Valid @NotNull(message = "O Id do ticket nao pode ser nulo") Long idTicket) {
        return ticketRepository.findById(idTicket).orElseThrow(() -> new TicketNaoEncontradoException(idTicket));
    }

    public void filtrarPorCategoria() {

    }

    public List<Ticket> buscarTicketsPorStatusEspecifico(Usuario usuario, Status status) {
        boolean isAdmin = usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"));
        Status statusTipo = statusRepository.findByIdStatus(status.getIdStatus()).orElseThrow(() -> new StatusNaoEncontradoException("Status com tipo " + status.getNome() + " nao encontrado"));
        if(isAdmin) {
            return ticketRepository.findByStatus(statusTipo).stream().toList();
        } 
        return null;
    }

    public List<Ticket> buscarTicketsPorPeriodo(Instant dataInicio, Instant dataFim) {
        List<Ticket> ticketsNoPeriodo = ticketRepository.findByDataCriacaoBetween(dataInicio, dataFim);
        if(ticketsNoPeriodo.isEmpty()) {
            throw new TicketNaoEncontradoException("Nao encontrado tickets no periodo selecionado");
        }

        return ticketsNoPeriodo;
    }



}
