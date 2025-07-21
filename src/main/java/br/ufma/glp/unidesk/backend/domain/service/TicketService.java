package br.ufma.glp.unidesk.backend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final StorageService storageService;

    public TicketService(TicketRepository ticketRepository, StorageService storageService) {
        this.ticketRepository = ticketRepository;
        this.storageService = storageService;
    }

    public List<Ticket> listarTickets(Status status, Coordenacao coordenacao, Prioridade prioridade) {
        List<Ticket> tickets = ticketRepository.findAll();
        // diferenciar aluno e coordenação
        return tickets;
    }

    @Transactional
    public Ticket novoTicket(@Valid @NotNull(message = "O Ticket nao pode ser nulo") Ticket ticket, MultipartFile file) throws Exception {
        if(file != null) {
            String idFile = storageService.uploadFile(file);
            ticket.setIdFile(idFile);
        }

        ticketRepository.save(ticket);
        return ticket;
    }

    public void alterarStatusTicket() {

    }

    public void fecharTicket() {

    }

    public void buscarTicketPorId() {

    }

    public void buscarTicketPorTermo() {

    }

    public void filtrarPorCategoria() {

    }


}
