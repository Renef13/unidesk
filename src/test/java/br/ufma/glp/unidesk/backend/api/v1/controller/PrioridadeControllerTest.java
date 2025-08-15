package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.PrioridadeCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.PrioridadeEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.PrioridadeModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.PrioridadeCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.PrioridadeEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.PrioridadeModel;
import br.ufma.glp.unidesk.backend.domain.exception.PrioridadeNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import br.ufma.glp.unidesk.backend.domain.service.PrioridadeService;
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

@WebMvcTest(controllers = PrioridadeController.class)
class PrioridadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrioridadeService prioridadeService;

    @MockBean
    private PrioridadeModelAssembler prioridadeModelAssembler;

    @MockBean
    private PrioridadeCadastroInputDisassembler prioridadeCadastroInputDisassembler;

    @MockBean
    private PrioridadeEdicaoInputDisassembler prioridadeEdicaoInputDisassembler;

    @Test
    @DisplayName("Deve listar todas as prioridades quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodasPrioridadesQuandoAdminAutenticado() throws Exception {
        PrioridadeModel model = new PrioridadeModel();
        model.setIdPrioridade(1L);
        model.setNivel("Alta");

        when(prioridadeService.listarTodos()).thenReturn(List.of(new Prioridade()));
        when(prioridadeModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/prioridades/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPrioridade").value(1))
                .andExpect(jsonPath("$[0].nivel").value("Alta"));

        verify(prioridadeService).listarTodos();
        verify(prioridadeModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando usuário não autenticado tenta listar prioridades")
    void deveRetornarUnauthorizedQuandoNaoAutenticadoTentaListar() throws Exception {
        mockMvc.perform(get("/v1/prioridades/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(prioridadeService);
        verifyNoInteractions(prioridadeModelAssembler);
    }

    @Test
    @DisplayName("Deve buscar prioridade por ID quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveBuscarPrioridadePorIdQuandoAdminAutenticado() throws Exception {
        PrioridadeModel model = new PrioridadeModel();
        model.setIdPrioridade(1L);
        model.setNivel("Alta");

        when(prioridadeService.buscarPorIdOuFalhar(1L)).thenReturn(new Prioridade());
        when(prioridadeModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/prioridades/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPrioridade").value(1))
                .andExpect(jsonPath("$.nivel").value("Alta"));

        verify(prioridadeService).buscarPorIdOuFalhar(1L);
        verify(prioridadeModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar prioridade por ID inexistente")
    @WithMockUser(roles = "ADMIN")
    void deveRetornarNotFoundQuandoBuscarPrioridadePorIdInexistente() throws Exception {
        when(prioridadeService.buscarPorIdOuFalhar(99L))
                .thenThrow(new PrioridadeNaoEncontradaException(99L));

        mockMvc.perform(get("/v1/prioridades/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(prioridadeService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @DisplayName("Deve buscar prioridade por nível quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveBuscarPrioridadePorNivelQuandoAdminAutenticado() throws Exception {
        PrioridadeModel model = new PrioridadeModel();
        model.setIdPrioridade(1L);
        model.setNivel("Alta");

        when(prioridadeService.buscarPorNivelOuFalhar("Alta")).thenReturn(new Prioridade());
        when(prioridadeModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/prioridades/nivel")
                        .param("nivel", "Alta")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPrioridade").value(1))
                .andExpect(jsonPath("$.nivel").value("Alta"));

        verify(prioridadeService).buscarPorNivelOuFalhar("Alta");
        verify(prioridadeModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve buscar prioridades por texto contido no nível quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveBuscarPrioridadesPorTextoContidoNoNivelQuandoAdminAutenticado() throws Exception {
        PrioridadeModel model = new PrioridadeModel();
        model.setIdPrioridade(1L);
        model.setNivel("Alta");

        when(prioridadeService.buscarPorNivelContendo("Alt")).thenReturn(List.of(new Prioridade()));
        when(prioridadeModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/prioridades/buscar")
                        .param("texto", "Alt")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPrioridade").value(1))
                .andExpect(jsonPath("$[0].nivel").value("Alta"));

        verify(prioridadeService).buscarPorNivelContendo("Alt");
        verify(prioridadeModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve adicionar prioridade quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveAdicionarPrioridadeQuandoAdminAutenticado() throws Exception {
        PrioridadeCadastroInput input = new PrioridadeCadastroInput();
        input.setNivel("Nova Prioridade");

        Prioridade domain = new Prioridade();
        PrioridadeModel model = new PrioridadeModel();
        model.setIdPrioridade(1L);
        model.setNivel("Nova Prioridade");

        when(prioridadeCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(prioridadeService.salvar(domain)).thenReturn(domain);
        when(prioridadeModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/prioridades/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPrioridade").value(1))
                .andExpect(jsonPath("$.nivel").value("Nova Prioridade"));

        verify(prioridadeCadastroInputDisassembler).toDomainObject(any());
        verify(prioridadeService).salvar(domain);
        verify(prioridadeModelAssembler).toModel(domain);
    }

    @Test
    @DisplayName("Deve atualizar prioridade quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarPrioridadeQuandoAdminAutenticado() throws Exception {
        PrioridadeEdicaoInput input = new PrioridadeEdicaoInput();
        input.setNivel("Prioridade Atualizada");

        Prioridade existente = new Prioridade();
        Prioridade atualizada = new Prioridade();
        PrioridadeModel model = new PrioridadeModel();
        model.setIdPrioridade(1L);
        model.setNivel("Prioridade Atualizada");

        when(prioridadeService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(prioridadeEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(prioridadeService.atualizar(existente)).thenReturn(atualizada);
        when(prioridadeModelAssembler.toModel(atualizada)).thenReturn(model);

        mockMvc.perform(put("/v1/prioridades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPrioridade").value(1))
                .andExpect(jsonPath("$.nivel").value("Prioridade Atualizada"));

        verify(prioridadeService).buscarPorIdOuFalhar(1L);
        verify(prioridadeEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(prioridadeService).atualizar(existente);
        verify(prioridadeModelAssembler).toModel(atualizada);
    }

    @Test
    @DisplayName("Deve remover prioridade quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverPrioridadeQuandoAdminAutenticado() throws Exception {
        doNothing().when(prioridadeService).excluir(1L);

        mockMvc.perform(delete("/v1/prioridades/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(prioridadeService).excluir(1L);
    }

    @Test
    @DisplayName("Deve retornar bad request quando dados inválidos no adicionar")
    @WithMockUser(roles = "ADMIN")
    void deveRetornarBadRequestQuandoDadosInvalidosNoAdicionar() throws Exception {
        PrioridadeCadastroInput input = new PrioridadeCadastroInput();

        mockMvc.perform(post("/v1/prioridades/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(prioridadeService);
    }

    @Test
    @DisplayName("Deve retornar not found quando tentar atualizar prioridade inexistente")
    @WithMockUser(roles = "ADMIN")
    void deveRetornarNotFoundQuandoTentarAtualizarPrioridadeInexistente() throws Exception {
        PrioridadeEdicaoInput input = new PrioridadeEdicaoInput();
        input.setNivel("Prioridade Atualizada");

        when(prioridadeService.buscarPorIdOuFalhar(99L))
                .thenThrow(new PrioridadeNaoEncontradaException(99L));

        mockMvc.perform(put("/v1/prioridades/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(prioridadeService).buscarPorIdOuFalhar(99L);
    }
}