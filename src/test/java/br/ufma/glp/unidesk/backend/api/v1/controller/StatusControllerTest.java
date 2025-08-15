package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.StatusCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.StatusEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.StatusModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.StatusCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.StatusEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.StatusModel;
import br.ufma.glp.unidesk.backend.domain.exception.StatusNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import br.ufma.glp.unidesk.backend.domain.service.StatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StatusController.class)
class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatusService statusService;

    @MockBean
    private StatusModelAssembler statusModelAssembler;

    @MockBean
    private StatusCadastroInputDisassembler statusCadastroInputDisassembler;

    @MockBean
    private StatusEdicaoInputDisassembler statusEdicaoInputDisassembler;

    @Test
    @DisplayName("Deve listar todos os status quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosStatusQuandoAdminAutenticado() throws Exception {
        StatusModel model = new StatusModel();
        model.setIdStatus(1L);
        model.setNome("Status Teste");

        when(statusService.listarTodos()).thenReturn(List.of(new Status()));
        when(statusModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/status/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idStatus").value(1))
                .andExpect(jsonPath("$[0].nome").value("Status Teste"));

        verify(statusService).listarTodos();
        verify(statusModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando não autenticado tenta listar status")
    void deveRetornarUnauthorizedQuandoNaoAutenticadoTentaListar() throws Exception {
        mockMvc.perform(get("/v1/status/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(statusService);
        verifyNoInteractions(statusModelAssembler);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve buscar status por nome quando ADMIN está autenticado")
    void deveBuscarStatusPorNomeQuandoAdminAutenticado() throws Exception {
        StatusModel model = new StatusModel();
        model.setIdStatus(1L);
        model.setNome("Status Teste");

        when(statusService.buscarPorNomeContendo("Teste")).thenReturn(List.of(new Status()));
        when(statusModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/status/buscar")
                        .param("nome", "Teste")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idStatus").value(1))
                .andExpect(jsonPath("$[0].nome").value("Status Teste"));

        verify(statusService).buscarPorNomeContendo("Teste");
        verify(statusModelAssembler).toCollectionModel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve buscar status por ID quando ADMIN está autenticado")
    void deveBuscarStatusPorIdQuandoAdminAutenticado() throws Exception {
        StatusModel model = new StatusModel();
        model.setIdStatus(1L);
        model.setNome("Status Teste");

        when(statusService.buscarPorIdOuFalhar(1L)).thenReturn(new Status());
        when(statusModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/status/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStatus").value(1))
                .andExpect(jsonPath("$.nome").value("Status Teste"));

        verify(statusService).buscarPorIdOuFalhar(1L);
        verify(statusModelAssembler).toModel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar not found quando buscar status por ID inexistente")
    void deveRetornarNotFoundQuandoBuscarStatusPorIdInexistente() throws Exception {
        when(statusService.buscarPorIdOuFalhar(99L))
                .thenThrow(new StatusNaoEncontradoException(99L));

        mockMvc.perform(get("/v1/status/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(statusService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve adicionar status quando ADMIN está autenticado")
    void deveAdicionarStatusQuandoAdminAutenticado() throws Exception {
        StatusCadastroInput input = new StatusCadastroInput();
        input.setNome("Novo Status");

        Status domain = new Status();
        StatusModel model = new StatusModel();
        model.setIdStatus(1L);
        model.setNome("Novo Status");

        when(statusCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(statusService.salvar(domain)).thenReturn(domain);
        when(statusModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/status/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idStatus").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Status"));

        verify(statusCadastroInputDisassembler).toDomainObject(any());
        verify(statusService).salvar(domain);
        verify(statusModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve atualizar status quando ADMIN está autenticado")
    void deveAtualizarStatusQuandoAdminAutenticado() throws Exception {
        StatusEdicaoInput input = new StatusEdicaoInput();
        input.setNome("Status Atualizado");

        Status existente = new Status();
        Status atualizado = new Status();
        StatusModel model = new StatusModel();
        model.setIdStatus(1L);
        model.setNome("Status Atualizado");

        when(statusService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(statusEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(statusService.atualizar(existente)).thenReturn(atualizado);
        when(statusModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/status/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStatus").value(1))
                .andExpect(jsonPath("$.nome").value("Status Atualizado"));

        verify(statusService).buscarPorIdOuFalhar(1L);
        verify(statusEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(statusService).atualizar(existente);
        verify(statusModelAssembler).toModel(atualizado);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve remover status quando ADMIN está autenticado")
    void deveRemoverStatusQuandoAdminAutenticado() throws Exception {
        doNothing().when(statusService).excluir(1L);

        mockMvc.perform(delete("/v1/status/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(statusService).excluir(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar bad request quando dados inválidos no adicionar")
    void deveRetornarBadRequestQuandoDadosInvalidosNoAdicionar() throws Exception {
        StatusCadastroInput input = new StatusCadastroInput();

        mockMvc.perform(post("/v1/status/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(statusService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar not found quando tentar atualizar status inexistente")
    void deveRetornarNotFoundQuandoTentarAtualizarStatusInexistente() throws Exception {
        StatusEdicaoInput input = new StatusEdicaoInput();
        input.setNome("Status Atualizado");

        when(statusService.buscarPorIdOuFalhar(99L))
                .thenThrow(new StatusNaoEncontradoException(99L));

        mockMvc.perform(put("/v1/status/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(statusService).buscarPorIdOuFalhar(99L);
        verifyNoInteractions(statusModelAssembler);
    }
}