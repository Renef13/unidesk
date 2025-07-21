package br.ufma.glp.unidesk.backend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.model.UsuarioRole;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class TicketService {
    
    private TicketRepository ticketRepository;
    private StorageService storageService;

    public TicketService(TicketRepository ticketRepository, StorageService storageService) {
        this.ticketRepository = ticketRepository;
        this.storageService = storageService;
    }

    public List<Ticket> listarTickets(Usuario usuario) {
        if(usuario.getRole().equals(UsuarioRole.ADMIN)) {
            return ticketRepository.findAll().stream().toList();
        } else {
            return ticketRepository.findByAlunoIdUsuario(usuario.getIdUsuario()).stream().toList();
        }
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
