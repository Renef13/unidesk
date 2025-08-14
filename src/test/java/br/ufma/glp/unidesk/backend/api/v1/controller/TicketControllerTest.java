package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.TicketCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.TicketEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.TicketModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.TicketMovimentacaoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.TicketCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.TicketEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.*;
import br.ufma.glp.unidesk.backend.domain.exception.TicketNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.*;
import br.ufma.glp.unidesk.backend.domain.service.AuthService;
import br.ufma.glp.unidesk.backend.domain.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private TicketModelAssembler ticketModelAssembler;

    @MockBean
    private TicketCadastroInputDisassembler ticketCadastroInputDisassembler;

    @MockBean
    private TicketEdicaoInputDisassembler ticketEdicaoInputDisassembler;

    @MockBean
    private AuthService authService;

    @MockBean
    private TicketMovimentacaoModelAssembler ticketMovimentacaoModelAssembler;


    @Test
    @DisplayName("Deve buscar ticket por ID quando existir")
    @WithMockUser
    void deveBuscarTicketPorIdQuandoExistir() throws Exception {
        TicketModel model = new TicketModel();
        model.setIdTicket(1L);
        model.setTitulo("Problema com matrícula");

        when(ticketService.buscarTicketPorId(1L)).thenReturn(new Ticket());
        when(ticketService.getUrlImage(1L)).thenReturn("http://example.com/image.jpg");
        when(ticketModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/tickets/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket").value(1))
                .andExpect(jsonPath("$.titulo").value("Problema com matrícula"))
                .andExpect(jsonPath("$.urlImage").value("http://example.com/image.jpg"));

        verify(ticketService).buscarTicketPorId(1L);
        verify(ticketService).getUrlImage(1L);
        verify(ticketModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar ticket por ID inexistente")
    @WithMockUser
    void deveRetornarNotFoundQuandoBuscarTicketPorIdInexistente() throws Exception {
        when(ticketService.buscarTicketPorId(99L)).thenThrow(new TicketNaoEncontradoException(99L));

        mockMvc.perform(get("/v1/tickets/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(ticketService).buscarTicketPorId(99L);
    }

    @Test
    @DisplayName("Deve filtrar tickets por categoria")
    @WithMockUser
    void deveFiltrarTicketsPorCategoria() throws Exception {
        TicketModel model = new TicketModel();
        model.setIdTicket(1L);
        model.setTitulo("Problema com matrícula");

        when(ticketService.filtrarPorCategoria("Matrícula")).thenReturn(List.of(new Ticket()));
        when(ticketModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/tickets/categoria")
                        .param("nomeCategoria", "Matrícula")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTicket").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Problema com matrícula"));

        verify(ticketService).filtrarPorCategoria("Matrícula");
        verify(ticketModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve adicionar ticket quando aluno autenticado")
    @WithMockUser(roles = "ALUNO")
    void deveAdicionarTicketQuandoAlunoAutenticado() throws Exception {
        TicketCadastroInput input = new TicketCadastroInput();
        input.setTitulo("Novo Ticket");
        input.setDescricao("Descrição do problema");
        input.setIdCoordenacao(1L);
        input.setIdAluno(1L);
        input.setIdStatus(1L);
        input.setIdPrioridade(1L);
        input.setIdCategoria(1L);

        Ticket domain = new Ticket();
        TicketModel model = new TicketModel();
        model.setIdTicket(1L);
        model.setTitulo("Novo Ticket");

        when(ticketCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(ticketService.novoTicket(domain)).thenReturn(domain);
        when(ticketModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/tickets/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTicket").value(1))
                .andExpect(jsonPath("$.titulo").value("Novo Ticket"));

        verify(ticketCadastroInputDisassembler).toDomainObject(any());
        verify(ticketService).novoTicket(domain);
        verify(ticketModelAssembler).toModel(domain);
    }

    @Test
    @DisplayName("Deve atualizar ticket quando aluno autenticado")
    @WithMockUser(roles = "ALUNO")
    void deveAtualizarTicketQuandoAlunoAutenticado() throws Exception {
        TicketEdicaoInput input = new TicketEdicaoInput();
        input.setTitulo("Ticket Atualizado");
        input.setDescricao("Descrição atualizada");

        Ticket ticketExistente = new Ticket();
        Ticket ticketAtualizado = new Ticket();
        TicketModel model = new TicketModel();
        model.setIdTicket(1L);
        model.setTitulo("Ticket Atualizado");

        doNothing().when(ticketEdicaoInputDisassembler).copyToDomainObject(input, ticketExistente);
        when(ticketService.atualizarTicket(eq(1L), any())).thenReturn(ticketAtualizado);
        when(ticketModelAssembler.toModel(ticketAtualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket").value(1))
                .andExpect(jsonPath("$.titulo").value("Ticket Atualizado"));

        verify(ticketEdicaoInputDisassembler).copyToDomainObject(any(), any());
        verify(ticketService).atualizarTicket(eq(1L), any());
        verify(ticketModelAssembler).toModel(ticketAtualizado);
    }

    @Test
    @DisplayName("Deve alterar status do ticket quando funcionário autorizado")
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    void deveAlterarStatusTicketQuandoFuncionarioAutorizado() throws Exception {
        TicketEdicaoInput input = new TicketEdicaoInput();
        input.setIdStatus(2L);

        Ticket ticket = new Ticket();
        Ticket ticketAtualizado = new Ticket();
        TicketModel model = new TicketModel();
        model.setIdTicket(1L);

        when(ticketService.buscarTicketPorId(1L)).thenReturn(ticket);
        doNothing().when(ticketEdicaoInputDisassembler).copyToDomainObject(input, ticket);
        when(ticketService.alterarStatusTicket(eq(1L), any())).thenReturn(ticketAtualizado);
        when(ticketModelAssembler.toModel(ticketAtualizado)).thenReturn(model);

        mockMvc.perform(patch("/v1/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket").value(1));

        verify(ticketService).buscarTicketPorId(1L);
        verify(ticketEdicaoInputDisassembler).copyToDomainObject(input, ticket);
        verify(ticketService).alterarStatusTicket(eq(1L), any());
        verify(ticketModelAssembler).toModel(ticketAtualizado);
    }

    @Test
    @DisplayName("Deve fechar ticket quando funcionário autorizado")
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    void deveFecharTicketQuandoFuncionarioAutorizado() throws Exception {
        Ticket ticketFechado = new Ticket();
        TicketModel model = new TicketModel();
        model.setIdTicket(1L);
        model.setDataFechamento(Instant.now());

        when(ticketService.fecharTicket(1L)).thenReturn(ticketFechado);
        when(ticketModelAssembler.toModel(ticketFechado)).thenReturn(model);

        mockMvc.perform(patch("/v1/tickets/1/fechar")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket").value(1))
                .andExpect(jsonPath("$.dataFechamento").exists());

        verify(ticketService).fecharTicket(1L);
        verify(ticketModelAssembler).toModel(ticketFechado);
    }

    @Test
    @WithMockUser(roles = "ALUNO")
    @DisplayName("Deve retornar tickets por mês quando autenticado")
    void deveRetornarTicketsPorMesQuandoAutenticado() throws Exception {
        Usuario usuario = new Aluno();
        TicketPorMesModel janeiro = new TicketPorMesModel(1, 5L);
        TicketPorMesModel fevereiro = new TicketPorMesModel(2, 8L);
        List<TicketPorMesModel> ticketsPorMes = List.of(janeiro, fevereiro);

        when(authService.getCurrentUsuarioEntity()).thenReturn(usuario);
        when(ticketService.obterTicketsPorMesFechado(usuario)).thenReturn(ticketsPorMes);

        mockMvc.perform(get("/v1/tickets/tickets-por-mes")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mes").value(1))
                .andExpect(jsonPath("$[0].total").value(5))
                .andExpect(jsonPath("$[1].mes").value(2))
                .andExpect(jsonPath("$[1].total").value(8));

        verify(authService).getCurrentUsuarioEntity();
        verify(ticketService).obterTicketsPorMesFechado(usuario);
    }

    @Test
    @DisplayName("Deve listar movimentações de um ticket quando autenticado")
    @WithMockUser
    void deveListarMovimentacoesDeUmTicketQuandoAutenticado() throws Exception {
        TicketMovimentacaoModel model = new TicketMovimentacaoModel();
        model.setIdMovimentacao(1L);
        model.setTipo(TipoMovimentacao.ATUALIZAR_STATUS);
        model.setDataMovimentacao(Instant.now());

        when(ticketService.buscarMovimentacoesTicket(1L)).thenReturn(List.of(new TicketMovimentacao()));
        when(ticketMovimentacaoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/tickets/movimentacoes/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMovimentacao").value(1))
                .andExpect(jsonPath("$[0].tipo").value("ATUALIZAR_STATUS"));

        verify(ticketService).buscarMovimentacoesTicket(1L);
        verify(ticketMovimentacaoModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar bad request quando dados inválidos ao adicionar ticket")
    @WithMockUser(roles = "ALUNO")
    void deveRetornarBadRequestQuandoDadosInvalidosAoAdicionarTicket() throws Exception {
        TicketCadastroInput input = new TicketCadastroInput();

        mockMvc.perform(post("/v1/tickets/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(ticketService);
    }
}