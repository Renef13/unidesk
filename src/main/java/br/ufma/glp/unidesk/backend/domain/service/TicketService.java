package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.StatusNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.TicketNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.*;
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
    private final StatusService statusService;
    private final CoordenadorService coordenadorService;
    private final FuncionarioCoordenacaoService funcionarioCoordenacaoService;

    public Page<Ticket> listarTickets(@Valid @NotNull Usuario usuario, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        boolean isAdmin = usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"));
        if (isAdmin) {
            return ticketRepository.findAll(pageable);
        } else if (usuario.getRole() == UsuarioRole.ALUNO) {
            return ticketRepository.findByAlunoIdUsuario(usuario.getIdUsuario(), pageable);
        } else if (usuario.getRole() == UsuarioRole.COORDENADOR) {
            Coordenacao coord = coordenadorService.buscarPorIdOuFalhar(usuario.getIdUsuario()).getCoordenacao();
            return ticketRepository.findByCoordenacaoIdCoordenacao(coord.getIdCoordenacao(), pageable);
        } else if (usuario.getRole() == UsuarioRole.FUNCIONARIO_COORDENACAO) {
            FuncionarioCoordenacao func = funcionarioCoordenacaoService.buscarPorIdOuFalhar(usuario.getIdUsuario());
            return ticketRepository.findByFuncionarioIdUsuario(func.getIdUsuario(), pageable);
        } else {
            return Page.empty(pageable);
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
    public Ticket alterarStatusTicket(@NotNull Long idTicket, @Valid @NotNull Ticket ticket) {
        ticketRepository.findById(idTicket)
                .orElseThrow(() -> new TicketNaoEncontradoException(idTicket));

        Status status = statusRepository.findById(ticket.getStatus().getIdStatus())
                .orElseThrow(() -> new StatusNaoEncontradoException("Status não encontrado para o id: " + ticket.getStatus().getIdStatus()));

        ticket.setStatus(status);

        return ticketRepository.save(ticket);
    }


    @Transactional
    public Ticket fecharTicket(@NotNull Long idTicket) {
        Ticket ticketExistente = ticketRepository.findById(idTicket)
                .orElseThrow(() -> new TicketNaoEncontradoException(idTicket));

        Status statusFechado = statusService.buscarPorNomeOuFalhar("Fechado");

        if (ticketExistente.getStatus().getIdStatus().equals(statusFechado.getIdStatus())) {
            throw new IllegalStateException("O ticket já está fechado.");
        }

        ticketExistente.setStatus(statusFechado);

        return ticketRepository.save(ticketExistente);
    }


    @Transactional
    public Ticket atualizarTicket(@Valid @NotNull Ticket ticketAtualizado) {
        //TODO: Ajustar depois para os campos certos ou dividis em outras funcoes
        Ticket ticketExistente = ticketRepository.findById(ticketAtualizado.getIdTicket())
                .orElseThrow(() -> new TicketNaoEncontradoException(ticketAtualizado.getIdTicket()));

        ticketExistente.setTitulo(ticketAtualizado.getTitulo());
        ticketExistente.setDescricao(ticketAtualizado.getDescricao());
        ticketExistente.setCoordenacao(ticketAtualizado.getCoordenacao());
        ticketExistente.setFuncionario(ticketAtualizado.getFuncionario());
        ticketExistente.setStatus(ticketAtualizado.getStatus());
        ticketExistente.setIdFile(ticketAtualizado.getIdFile());

        return ticketRepository.save(ticketExistente);
    }

    public Ticket buscarTicketPorId(@Valid @NotNull(message = "O Id do ticket nao pode ser nulo") Long idTicket) {
        return ticketRepository.findById(idTicket)
                .orElseThrow(() -> new TicketNaoEncontradoException(idTicket));
    }

    public List<Ticket> filtrarPorCategoria(@NotNull String nomeCategoria) {
        List<Ticket> tickets = ticketRepository.findByCategoriaNomeIgnoreCase(nomeCategoria);
        if (tickets.isEmpty()) {
            throw new TicketNaoEncontradoException("Nenhum ticket encontrado para a categoria: " + nomeCategoria);
        }
        return tickets;
    }

    public List<Ticket> buscarTicketsPorStatusEspecifico(@Valid @NotNull Usuario usuario, @Valid @NotNull Long idStatus) {

        boolean isAdmin = usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"));
        Status statusTipo = statusRepository.findByIdStatus(idStatus)
                .orElseThrow(() -> new StatusNaoEncontradoException("Status com id " + idStatus + " nao encontrado"));
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