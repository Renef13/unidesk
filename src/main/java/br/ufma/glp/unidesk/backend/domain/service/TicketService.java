package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.DashboardModel;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.TicketPorMesModel;
import br.ufma.glp.unidesk.backend.domain.exception.CoordenacaoNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.exception.FuncionarioCoordenacaoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.StatusNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.TicketNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.repository.CoordenacaoRepository;
import br.ufma.glp.unidesk.backend.domain.repository.FuncionarioCoordenacaoRepository;
import br.ufma.glp.unidesk.backend.domain.model.*;
import br.ufma.glp.unidesk.backend.domain.repository.StatusRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
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

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StorageService storageService;
    private final StatusRepository statusRepository;
    private final StatusService statusService;
    private final CoordenacaoRepository coordenacaoRepository;
    private final FuncionarioCoordenacaoRepository funcionarioCoordenacaoRepository;
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
    public Ticket novoTicket(@Valid @NotNull(message = "O Ticket nao pode ser nulo") Ticket ticket, MultipartFile file)
            throws Exception {
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
                .orElseThrow(() -> new StatusNaoEncontradoException(
                        "Status não encontrado para o id: " + ticket.getStatus().getIdStatus()));

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
    public Ticket atualizarTicket(@NotNull Ticket ticketAtualizado) {
        // TODO: Ajustar depois para os campos certos ou dividis em outras funcoes
        Ticket ticketExistente = ticketRepository.findById(ticketAtualizado.getIdTicket())
                .orElseThrow(() -> new TicketNaoEncontradoException(ticketAtualizado.getIdTicket()));
        if (ticketAtualizado.getTitulo() != null) {
            ticketExistente.setTitulo(ticketAtualizado.getTitulo());
        }
        if (ticketAtualizado.getDescricao() != null) {
            ticketExistente.setDescricao(ticketAtualizado.getDescricao());
        }
        if (ticketAtualizado.getCoordenacao() != null) {
            Coordenacao coordenacao = coordenacaoRepository
                    .findById(ticketAtualizado.getCoordenacao().getIdCoordenacao())
                    .orElseThrow(() -> new CoordenacaoNaoEncontradaException("Coordenacao nao encontrada"));
            ticketExistente.setCoordenacao(coordenacao);
        }
        if (ticketAtualizado.getFuncionario() != null) {
            FuncionarioCoordenacao funcionarioCoord = funcionarioCoordenacaoRepository
                    .findById(ticketAtualizado.getFuncionario().getIdUsuario())
                    .orElseThrow(() -> new FuncionarioCoordenacaoNaoEncontradoException("Funcionario nao encontrado"));
            ticketExistente.setFuncionario(funcionarioCoord);
        }
        if (ticketAtualizado.getStatus() != null) {
            Status status = statusRepository.findById(ticketAtualizado.getStatus().getIdStatus())
                    .orElseThrow(() -> new StatusNaoEncontradoException("Status nao encontrado"));
            ticketExistente.setStatus(status);
        }
        if (ticketAtualizado.getIdFile() != null) {
            ticketExistente.setIdFile(ticketAtualizado.getIdFile());
        }

        return ticketExistente;
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

    public String getUrlImage(Long idTicket) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            XmlParserException, ServerException, IllegalArgumentException, IOException {
        String idFile = ticketRepository.findByIdTicket(idTicket).get().getIdFile();
        return storageService.getUrl(idFile);
    }

    public List<Ticket> buscarTicketsPorStatusEspecifico(@Valid @NotNull Usuario usuario,
            @Valid @NotNull Long idStatus) {

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

    public DashboardModel dashboardTickets() {
        Long totalTickets = ticketRepository.count();

        Map<String, Long> contagens = countTicketsByAllStatus();

        long totalTicketsResolvidos = contagens.getOrDefault("Fechado", 0L);
        long totalTicketsAbertos = contagens.getOrDefault("Aberto", 0L);
        long totalTicketsPendentes = contagens.getOrDefault("Pendente", 0L);
        long totalTicketsEmAndamento = contagens.getOrDefault("Em Andamento", 0L);

        Long porcentagemProgresso = (totalTicketsEmAndamento + totalTicketsPendentes + totalTicketsResolvidos)
                / totalTickets * 100;
        Double porcentagemAbertos = (double) (totalTicketsAbertos / totalTickets) * 100;
        Long porcentagemResolvidos = totalTicketsResolvidos / totalTickets * 100;
        Long porcentagemAndamento = totalTicketsEmAndamento / totalTickets * 100;
        DashboardModel dashboardModel = new DashboardModel();
        dashboardModel.setTotalTickets(totalTickets);
        dashboardModel.setTotalTicketsResolvidos(totalTicketsResolvidos);
        dashboardModel.setTotalTicketsAbertos(totalTicketsAbertos);
        dashboardModel.setTotalTicketsPendentes(totalTicketsPendentes);
        dashboardModel.setTotalTicketsEmAndamento(totalTicketsEmAndamento);
        dashboardModel.setPorcentagemProgresso(porcentagemProgresso);
        dashboardModel.setPorcentagemAbertos(porcentagemAbertos);
        dashboardModel.setPorcentagemResolvidos(porcentagemResolvidos);
        dashboardModel.setPorcentagemAndamento(porcentagemAndamento);
        return dashboardModel;
    }

    public Map<String, Long> countTicketsByAllStatus() {
        List<Object[]> resultados = ticketRepository.countTicketsByStatus();
        return resultados.stream().collect(Collectors.toMap(
                r -> (String) r[0],
                r -> (Long) r[1]));
    }

    public List<TicketPorMesModel> obterTicketsPorMes() {
        Status statusFechado = statusRepository.findByNome("Fechado").get();
        List<Ticket> tickets = ticketRepository.findByStatus(statusFechado);

        Map<Integer, Long> mapaMeses = tickets.stream()
                .collect(Collectors.groupingBy(t -> ZonedDateTime.ofInstant(t.getDataFechamento(), ZoneId.of("America/Sao_Paulo"))
                                .getMonthValue(),
                        Collectors.counting()));

        List<TicketPorMesModel> lista = new ArrayList<>();
        for (int mes = 1; mes <= 12; mes++) {
            lista.add(new TicketPorMesModel(mes, mapaMeses.getOrDefault(mes, 0L)));
        }

        return lista;
    }

}