package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.MensagemCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.MensagemEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.MensagemModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.MensagemCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.MensagemEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.MensagemModel;
import br.ufma.glp.unidesk.backend.domain.exception.MensagemNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.Mensagem;
import br.ufma.glp.unidesk.backend.domain.service.MensagemService;
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

@WebMvcTest(controllers = MensagemController.class)
class MensagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MensagemService mensagemService;

    @MockBean
    private MensagemModelAssembler mensagemModelAssembler;

    @MockBean
    private MensagemCadastroInputDisassembler mensagemCadastroInputDisassembler;

    @MockBean
    private MensagemEdicaoInputDisassembler mensagemEdicaoInputDisassembler;

    @Test
    @DisplayName("Deve listar todas as mensagens para qualquer usuário autenticado")
    @WithMockUser
    void deveListarTodasMensagensParaQualquerUsuarioAutenticado() throws Exception {
        MensagemModel model = new MensagemModel();
        model.setIdMensagem(1L);
        model.setConteudo("Conteúdo da mensagem");

        when(mensagemService.listarTodas()).thenReturn(List.of(new Mensagem()));
        when(mensagemModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/mensagens/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMensagem").value(1))
                .andExpect(jsonPath("$[0].conteudo").value("Conteúdo da mensagem"));

        verify(mensagemService).listarTodas();
        verify(mensagemModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando não autenticado tenta listar")
    void deveRetornarUnauthorizedQuandoNaoAutenticadoTentaListar() throws Exception {
        mockMvc.perform(get("/v1/mensagens/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(mensagemService);
        verifyNoInteractions(mensagemModelAssembler);
    }

    @Test
    @DisplayName("Deve buscar mensagem por ID quando existir")
    @WithMockUser
    void deveBuscarMensagemPorIdQuandoExistir() throws Exception {
        MensagemModel model = new MensagemModel();
        model.setIdMensagem(1L);
        model.setConteudo("Conteúdo da mensagem");

        when(mensagemService.buscarPorIdOuFalhar(1L)).thenReturn(new Mensagem());
        when(mensagemModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/mensagens/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMensagem").value(1))
                .andExpect(jsonPath("$.conteudo").value("Conteúdo da mensagem"));

        verify(mensagemService).buscarPorIdOuFalhar(1L);
        verify(mensagemModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar por ID inexistente")
    @WithMockUser
    void deveRetornarNotFoundQuandoBuscarPorIdInexistente() throws Exception {
        when(mensagemService.buscarPorIdOuFalhar(99L))
                .thenThrow(new MensagemNaoEncontradaException(99L));

        mockMvc.perform(get("/v1/mensagens/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(mensagemService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve adicionar mensagem quando usuário autenticado")
    void deveAdicionarMensagemQuandoUsuarioAutenticado() throws Exception {
        MensagemCadastroInput input = new MensagemCadastroInput();
        input.setConteudo("Nova mensagem");
        input.setIdTicket(1L);
        input.setIdUsuario(1L);

        Mensagem domain = new Mensagem();
        MensagemModel model = new MensagemModel();
        model.setIdMensagem(1L);
        model.setConteudo("Nova mensagem");

        when(mensagemCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(mensagemService.salvar(domain)).thenReturn(domain);
        when(mensagemModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/mensagens/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMensagem").value(1))
                .andExpect(jsonPath("$.conteudo").value("Nova mensagem"));

        verify(mensagemCadastroInputDisassembler).toDomainObject(any());
        verify(mensagemService).salvar(domain);
        verify(mensagemModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar mensagem quando usuário autenticado")
    void deveAtualizarMensagemQuandoUsuarioAutenticado() throws Exception {
        MensagemEdicaoInput input = new MensagemEdicaoInput();
        input.setConteudo("Mensagem atualizada");

        Mensagem existente = new Mensagem();
        Mensagem atualizada = new Mensagem();
        MensagemModel model = new MensagemModel();
        model.setIdMensagem(1L);
        model.setConteudo("Mensagem atualizada");

        when(mensagemService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(mensagemEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(mensagemService.atualizar(existente)).thenReturn(atualizada);
        when(mensagemModelAssembler.toModel(atualizada)).thenReturn(model);

        mockMvc.perform(put("/v1/mensagens/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMensagem").value(1))
                .andExpect(jsonPath("$.conteudo").value("Mensagem atualizada"));

        verify(mensagemService).buscarPorIdOuFalhar(1L);
        verify(mensagemEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(mensagemService).atualizar(existente);
        verify(mensagemModelAssembler).toModel(atualizada);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar not found quando tentar atualizar mensagem inexistente")
    void deveRetornarNotFoundQuandoTentarAtualizarMensagemInexistente() throws Exception {
        MensagemEdicaoInput input = new MensagemEdicaoInput();
        input.setConteudo("Mensagem atualizada");

        when(mensagemService.buscarPorIdOuFalhar(99L))
                .thenThrow(new MensagemNaoEncontradaException(99L));

        mockMvc.perform(put("/v1/mensagens/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(mensagemService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve remover mensagem quando usuário autenticado")
    void deveRemoverMensagemQuandoUsuarioAutenticado() throws Exception {
        doNothing().when(mensagemService).remover(1L);

        mockMvc.perform(delete("/v1/mensagens/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(mensagemService).remover(1L);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar not found quando tentar remover mensagem inexistente")
    void deveRetornarNotFoundQuandoTentarRemoverMensagemInexistente() throws Exception {
        doThrow(new MensagemNaoEncontradaException(99L))
                .when(mensagemService).remover(99L);

        mockMvc.perform(delete("/v1/mensagens/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(mensagemService).remover(99L);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar bad request quando dados inválidos no cadastro")
    void deveRetornarBadRequestQuandoDadosInvalidosNoCadastro() throws Exception {
        MensagemCadastroInput input = new MensagemCadastroInput();

        mockMvc.perform(post("/v1/mensagens/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(mensagemService);
    }

}