package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.DashboardModel;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.TicketPorMesModel;
import br.ufma.glp.unidesk.backend.domain.exception.CoordenacaoNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.exception.FuncionarioCoordenacaoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.StatusNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.exception.TicketNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.*;
import br.ufma.glp.unidesk.backend.domain.repository.*;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMovimentacaoRepository ticketMovimentacaoRepository;
    private final AuthService authService;
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
    public Ticket novoTicket(@Valid @NotNull(message = "O Ticket nao pode ser nulo") Ticket ticket /*,MultipartFile file*/)
            throws Exception {
        // if (file != null) {
        //     String idFile = storageService.uploadFile(file);
        //     ticket.setIdFile(idFile);
        // }
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket alterarStatusTicket(@NotNull Long idTicket, @Valid @NotNull Ticket ticket) {
        ticketRepository.findById(idTicket)
                .orElseThrow(() -> new TicketNaoEncontradoException(idTicket));

        Status status = statusRepository.findById(ticket.getStatus().getIdStatus())
                .orElseThrow(() -> new StatusNaoEncontradoException(
                        "Status não encontrado para o id: " + ticket.getStatus().getIdStatus()));
        if(status.getNome().equals("Fechado")) {
            ticket.setDataFechamento(Instant.now());
        }
        ticket.setStatus(status);

        Ticket saved = ticketRepository.save(ticket);
        if (status.getNome().equals("Fechado")) {
            registrarMovimentacao(saved, TipoMovimentacao.FINALIZAR, null);
        } else {
            registrarMovimentacao(saved, TipoMovimentacao.ATUALIZAR_STATUS, null);
        }
        return saved;
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
        ticketExistente.setDataFechamento(Instant.now());

        Ticket saved = ticketRepository.save(ticketExistente);
        registrarMovimentacao(saved, TipoMovimentacao.FINALIZAR, null);
        return saved;
    }

    @Transactional
    public Ticket atualizarTicket(@NotNull Long idTicket, @NotNull Ticket ticketAtualizado) {
        Ticket ticketExistente = ticketRepository.findById(idTicket)
                .orElseThrow(() -> new TicketNaoEncontradoException(idTicket));
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

        // Lógica para registrar movimentação apenas se o funcionário for alterado
        Long antigoFuncId = ticketExistente.getFuncionario() != null
                ? ticketExistente.getFuncionario().getIdUsuario() : null;
        if (ticketAtualizado.getFuncionario() != null) {
            Long novoFuncId = ticketAtualizado.getFuncionario().getIdUsuario();
            if (antigoFuncId == null || !antigoFuncId.equals(novoFuncId)) {
                FuncionarioCoordenacao funcionario = funcionarioCoordenacaoRepository
                        .findById(novoFuncId)
                        .orElseThrow(() -> new FuncionarioCoordenacaoNaoEncontradoException(
                                "Funcionario de coordenacao nao encontrado para o id: " + novoFuncId));
                ticketExistente.setFuncionario(funcionario);
                registrarMovimentacao(ticketExistente, TipoMovimentacao.DELEGAR, funcionario);
            }
        }

        // Atualiza o status se fornecido
        if (ticketAtualizado.getStatus() != null) {            
            Status status = statusRepository.findById(ticketAtualizado.getStatus().getIdStatus())
                .orElseThrow(() -> new StatusNaoEncontradoException(
                        "Status não encontrado para o id: " + ticketAtualizado.getStatus().getIdStatus()));
            if(status.getNome().equals("Fechado")) {
                ticketExistente.setDataFechamento(Instant.now());
            }
            ticketExistente.setStatus(status);
        }
        if (ticketAtualizado.getIdFile() != null) {
            ticketExistente.setIdFile(ticketAtualizado.getIdFile());
        }

        registrarMovimentacao(ticketExistente, TipoMovimentacao.ATUALIZAR, null);
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

    public String getUrlImage(Long idTicket) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, IllegalArgumentException, IOException {
        Ticket ticket = ticketRepository.findByIdTicket(idTicket)
                .orElseThrow(() -> new TicketNaoEncontradoException(idTicket));
        String idFile = ticket.getIdFile();
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


    public Page<Ticket> buscarTicketsComFiltros(Usuario usuario, String texto, Long statusId, Long prioridadeId, Long cursoId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        boolean isAdmin = usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"));
        Specification<Ticket> spec = Specification.where(null);

        if (!isAdmin) {
            if (usuario.getRole() == UsuarioRole.ALUNO) {
                spec = spec.and(TicketSpecs.hasAluno(usuario.getIdUsuario()));
            } else if (usuario.getRole() == UsuarioRole.COORDENADOR) {
                Long coordId = coordenadorService.buscarPorIdOuFalhar(usuario.getIdUsuario()).getCoordenacao().getIdCoordenacao();
                spec = spec.and(TicketSpecs.hasCoordenacao(coordId));
            } else if (usuario.getRole() == UsuarioRole.FUNCIONARIO_COORDENACAO) {
                Long funcId = funcionarioCoordenacaoService.buscarPorIdOuFalhar(usuario.getIdUsuario()).getIdUsuario();
                spec = spec.and(TicketSpecs.hasFuncionario(funcId));
            }
        }

        if (texto != null && !texto.isEmpty()) {
            spec = spec.and(TicketSpecs.hasText(texto));
        }
        if (statusId != null) {
            spec = spec.and(TicketSpecs.hasStatus(statusId));
        }
        if (prioridadeId != null) {
            spec = spec.and(TicketSpecs.hasPrioridade(prioridadeId));
        }
        if (cursoId != null) {
            spec = spec.and(TicketSpecs.hasCurso(cursoId));
        }

        return ticketRepository.findAll(spec, pageable);
    }

    public DashboardModel dashboardTickets(Usuario usuario) {
        
        Map<String, Long> contagens = countTicketsByAllStatus(usuario);

        long totalTicketsResolvidos = contagens.getOrDefault("Fechado", 0L);
        long totalTicketsAbertos = contagens.getOrDefault("Aberto", 0L);
        long totalTicketsPendentes = contagens.getOrDefault("Pendente", 0L);
        long totalTicketsEmAndamento = contagens.getOrDefault("Em Andamento", 0L);

        long totalTickets = totalTicketsAbertos + totalTicketsPendentes + totalTicketsResolvidos + totalTicketsEmAndamento;


        DashboardModel dashboardModel = new DashboardModel();
        if(totalTickets != 0){
            Double porcentagemProgresso = ((double)totalTicketsEmAndamento + totalTicketsPendentes + totalTicketsResolvidos)/ totalTickets * 100;
            Double porcentagemAbertos =  ((double)totalTicketsAbertos / totalTickets) * 100;
            Double porcentagemResolvidos = ((double)totalTicketsResolvidos / totalTickets) * 100;
            Double porcentagemAndamento = ((double)totalTicketsEmAndamento / totalTickets) * 100;
            dashboardModel.setPorcentagemProgresso(porcentagemProgresso);
            dashboardModel.setPorcentagemAbertos(porcentagemAbertos);
            dashboardModel.setPorcentagemResolvidos(porcentagemResolvidos);
            dashboardModel.setPorcentagemAndamento(porcentagemAndamento);
        }
        dashboardModel.setTotalTickets(totalTickets);
        dashboardModel.setTotalTicketsResolvidos(totalTicketsResolvidos);
        dashboardModel.setTotalTicketsAbertos(totalTicketsAbertos);
        dashboardModel.setTotalTicketsPendentes(totalTicketsPendentes);
        dashboardModel.setTotalTicketsEmAndamento(totalTicketsEmAndamento);
        return dashboardModel;
    }

    public Map<String, Long> countTicketsByAllStatus(Usuario usuario) {
        List<Object[]> resultados;

        if (usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"))) {
            resultados = ticketRepository.countTicketsByStatus();
        } else if (usuario.getRole() == UsuarioRole.ALUNO) {
            resultados = ticketRepository.countTicketsByStatusAndAluno(usuario.getIdUsuario());
        } else if (usuario.getRole() == UsuarioRole.COORDENADOR) {
            Coordenacao coord = coordenadorService.buscarPorIdOuFalhar(usuario.getIdUsuario()).getCoordenacao();
            resultados = ticketRepository.countTicketsByStatusAndCoordenacao(coord.getIdCoordenacao());
        } else if (usuario.getRole() == UsuarioRole.FUNCIONARIO_COORDENACAO) {
            FuncionarioCoordenacao func = funcionarioCoordenacaoService.buscarPorIdOuFalhar(usuario.getIdUsuario());
            resultados = ticketRepository.countTicketsByStatusAndFuncionario(func.getIdUsuario());
        } else {
            resultados = List.of();
        }

        return resultados.stream().collect(Collectors.toMap(r -> (String) r[0], r -> (Long) r[1]));
    }

    public List<TicketPorMesModel> obterTicketsPorMesFechado(Usuario usuario) {
        Status statusFechado = statusRepository.findByNome("Fechado").orElseThrow(() -> new StatusNaoEncontradoException("Status Fechado nao encontrado"));
        List<Ticket> tickets = null;
        if (usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"))) {
            tickets = ticketRepository.findByStatus(statusFechado);
        } else if (usuario.getRole() == UsuarioRole.ALUNO) {
            tickets = ticketRepository.findByAlunoIdUsuarioAndStatus(usuario.getIdUsuario(), statusFechado);
        } else if (usuario.getRole() == UsuarioRole.COORDENADOR) {
            Coordenacao coord = coordenadorService.buscarPorIdOuFalhar(usuario.getIdUsuario()).getCoordenacao();
            tickets = ticketRepository.findByCoordenacaoAndStatus(coord.getIdCoordenacao(), statusFechado);
        } else if (usuario.getRole() == UsuarioRole.FUNCIONARIO_COORDENACAO) {
            FuncionarioCoordenacao func = funcionarioCoordenacaoService.buscarPorIdOuFalhar(usuario.getIdUsuario());
            tickets = ticketRepository.findByFuncionarioAndStatus(func.getIdUsuario(), statusFechado);
        }

        if(tickets != null) {
            Map<Integer, Long> mapaMeses = tickets.stream()
                    .filter(t -> t.getDataFechamento() != null)
                    .collect(Collectors.groupingBy(t -> ZonedDateTime.ofInstant(t.getDataFechamento(), ZoneId.of("America/Sao_Paulo"))
                                    .getMonthValue(),
                            Collectors.counting()));
    
            List<TicketPorMesModel> lista = new ArrayList<>();
            for (int mes = 1; mes <= 12; mes++) {
                lista.add(new TicketPorMesModel(mes, mapaMeses.getOrDefault(mes, 0L)));
            }
    
            return lista;
        }
        return null;
    }


    @Transactional(readOnly = true)
    public List<TicketMovimentacao> buscarMovimentacoesTicket(Long idTicket) {
        if (!ticketRepository.existsById(idTicket)) {
            throw new TicketNaoEncontradoException(idTicket);
        }
        return ticketMovimentacaoRepository.findByTicketIdTicketOrderByDataMovimentacaoAsc(idTicket);
    }


    private void registrarMovimentacao(Ticket ticket, TipoMovimentacao tipo, Usuario usuarioDestino) {
        Usuario usuarioOrigem = authService.getCurrentUsuarioEntity();
        TicketMovimentacao mov = TicketMovimentacao.builder()
                .ticket(ticket)
                .tipo(tipo)
                .usuarioOrigem(usuarioOrigem)
                .usuarioDestino(usuarioDestino)
                .build();
        ticketMovimentacaoRepository.save(mov);
    }

}
