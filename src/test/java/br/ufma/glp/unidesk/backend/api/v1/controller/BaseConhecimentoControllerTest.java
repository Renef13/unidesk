package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.BaseConhecimentoModel;
import br.ufma.glp.unidesk.backend.domain.exception.BaseConhecimentoNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import br.ufma.glp.unidesk.backend.domain.service.BaseConhecimentoService;
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

@WebMvcTest(controllers = BaseConhecimentoController.class)
class BaseConhecimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BaseConhecimentoService baseConhecimentoService;

    @MockBean
    private BaseConhecimentoModelAssembler baseConhecimentoModelAssembler;

    @MockBean
    private BaseConhecimentoCadastroInputDisassembler baseConhecimentoCadastroInputDisassembler;

    @MockBean
    private BaseConhecimentoEdicaoInputDisassembler baseConhecimentoEdicaoInputDisassembler;

    @Test
    @DisplayName("Deve listar todos os itens da base de conhecimento para qualquer usuário")
    @WithMockUser
    void deveListarTodosOsItens() throws Exception {
        BaseConhecimentoModel model = new BaseConhecimentoModel();
        model.setIdArtigo(1L);
        model.setTitulo("FAQ Teste");
        model.setConteudo("Conteúdo de teste");

        when(baseConhecimentoService.listarTodos()).thenReturn(List.of(new BaseConhecimento()));
        when(baseConhecimentoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/faq/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idArtigo").value(1))
                .andExpect(jsonPath("$[0].titulo").value("FAQ Teste"))
                .andExpect(jsonPath("$[0].conteudo").value("Conteúdo de teste"));

        verify(baseConhecimentoService).listarTodos();
        verify(baseConhecimentoModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve buscar item por ID quando existir")
    @WithMockUser
    void deveBuscarPorIdQuandoExistir() throws Exception {
        BaseConhecimentoModel model = new BaseConhecimentoModel();
        model.setIdArtigo(1L);
        model.setTitulo("FAQ Teste");

        when(baseConhecimentoService.buscarPorIdOuFalhar(1L)).thenReturn(new BaseConhecimento());
        when(baseConhecimentoModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/faq/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idArtigo").value(1))
                .andExpect(jsonPath("$.titulo").value("FAQ Teste"));

        verify(baseConhecimentoService).buscarPorIdOuFalhar(1L);
        verify(baseConhecimentoModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar por ID inexistente")
    @WithMockUser
    void deveRetornarNotFoundQuandoBuscarPorIdInexistente() throws Exception {
        when(baseConhecimentoService.buscarPorIdOuFalhar(99L))
                .thenThrow(new BaseConhecimentoNaoEncontradaException(99L));

        mockMvc.perform(get("/v1/faq/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(baseConhecimentoService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @DisplayName("Deve buscar item por título quando existir")
    @WithMockUser
    void deveBuscarPorTituloQuandoExistir() throws Exception {
        BaseConhecimentoModel model = new BaseConhecimentoModel();
        model.setIdArtigo(1L);
        model.setTitulo("Como resetar senha");

        when(baseConhecimentoService.buscarPorTituloOuFalhar("Como resetar senha")).thenReturn(new BaseConhecimento());
        when(baseConhecimentoModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/faq/buscar")
                        .param("nome", "Como resetar senha")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idArtigo").value(1))
                .andExpect(jsonPath("$.titulo").value("Como resetar senha"));

        verify(baseConhecimentoService).buscarPorTituloOuFalhar("Como resetar senha");
        verify(baseConhecimentoModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar por título inexistente")
    @WithMockUser
    void deveRetornarNotFoundQuandoBuscarPorTituloInexistente() throws Exception {
        when(baseConhecimentoService.buscarPorTituloOuFalhar("Inexistente"))
                .thenThrow(new BaseConhecimentoNaoEncontradaException("Item não encontrado: Inexistente"));

        mockMvc.perform(get("/v1/faq/buscar")
                        .param("nome", "Inexistente")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(baseConhecimentoService).buscarPorTituloOuFalhar("Inexistente");
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve criar item quando coordenador autorizado")
    void deveCriarItemQuandoCoordenadorAutorizado() throws Exception {
        BaseConhecimentoCadastroInput input = new BaseConhecimentoCadastroInput();
        input.setTitulo("Nova FAQ");
        input.setConteudo("Conteúdo da nova FAQ");
        input.setIdCategoria(1L);

        BaseConhecimento domain = new BaseConhecimento();
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);
        domain.setCategoria(categoria);

        BaseConhecimentoModel model = new BaseConhecimentoModel();
        model.setIdArtigo(1L);
        model.setTitulo("Nova FAQ");

        when(baseConhecimentoCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(baseConhecimentoService.salvarBaseConhecimento(domain)).thenReturn(domain);
        when(baseConhecimentoModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/faq/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idArtigo").value(1))
                .andExpect(jsonPath("$.titulo").value("Nova FAQ"));

        verify(baseConhecimentoCadastroInputDisassembler).toDomainObject(any());
        verify(baseConhecimentoService).salvarBaseConhecimento(domain);
        verify(baseConhecimentoModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    @DisplayName("Deve criar item quando funcionário de coordenação autorizado")
    void deveCriarItemQuandoFuncionarioCoordenacaoAutorizado() throws Exception {
        BaseConhecimentoCadastroInput input = new BaseConhecimentoCadastroInput();
        input.setTitulo("Nova FAQ");
        input.setConteudo("Conteúdo da nova FAQ");
        input.setIdCategoria(1L);

        BaseConhecimento domain = new BaseConhecimento();
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);
        domain.setCategoria(categoria);

        BaseConhecimentoModel model = new BaseConhecimentoModel();
        model.setIdArtigo(1L);

        when(baseConhecimentoCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(baseConhecimentoService.salvarBaseConhecimento(domain)).thenReturn(domain);
        when(baseConhecimentoModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/faq/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idArtigo").value(1));

        verify(baseConhecimentoCadastroInputDisassembler).toDomainObject(any());
        verify(baseConhecimentoService).salvarBaseConhecimento(domain);
        verify(baseConhecimentoModelAssembler).toModel(domain);
    }

    @Test
    @DisplayName("Deve retornar unauthorized quando não autenticado tenta criar item")
    void deveRetornarUnauthorizedQuandoNaoAutenticadoTentaCriarItem() throws Exception {
        BaseConhecimentoCadastroInput input = new BaseConhecimentoCadastroInput();
        input.setTitulo("Nova FAQ");
        input.setConteudo("Conteúdo da nova FAQ");

        mockMvc.perform(post("/v1/faq/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(baseConhecimentoCadastroInputDisassembler);
        verifyNoInteractions(baseConhecimentoService);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve atualizar item quando coordenador autorizado")
    void deveAtualizarItemQuandoCoordenadorAutorizado() throws Exception {
        BaseConhecimentoEdicaoInput input = new BaseConhecimentoEdicaoInput();
        input.setTitulo("FAQ Atualizada");
        input.setConteudo("Conteúdo atualizado");

        BaseConhecimento existente = new BaseConhecimento();
        BaseConhecimento atualizado = new BaseConhecimento();
        BaseConhecimentoModel model = new BaseConhecimentoModel();
        model.setIdArtigo(1L);
        model.setTitulo("FAQ Atualizada");

        when(baseConhecimentoService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(baseConhecimentoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(baseConhecimentoService.atualizarBaseConhecimento(existente)).thenReturn(atualizado);
        when(baseConhecimentoModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/faq/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idArtigo").value(1))
                .andExpect(jsonPath("$.titulo").value("FAQ Atualizada"));

        verify(baseConhecimentoService).buscarPorIdOuFalhar(1L);
        verify(baseConhecimentoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(baseConhecimentoService).atualizarBaseConhecimento(existente);
        verify(baseConhecimentoModelAssembler).toModel(atualizado);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve deletar item quando coordenador autorizado")
    void deveDeletarItemQuandoCoordenadorAutorizado() throws Exception {
        doNothing().when(baseConhecimentoService).removerBaseConhecimento(1L);

        mockMvc.perform(delete("/v1/faq/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(baseConhecimentoService).removerBaseConhecimento(1L);
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    @DisplayName("Deve deletar item quando funcionário de coordenação autorizado")
    void deveDeletarItemQuandoFuncionarioCoordenacaoAutorizado() throws Exception {
        doNothing().when(baseConhecimentoService).removerBaseConhecimento(1L);

        mockMvc.perform(delete("/v1/faq/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(baseConhecimentoService).removerBaseConhecimento(1L);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve retornar bad request quando dados inválidos na criação")
    void deveRetornarBadRequestQuandoDadosInvalidosNaCriacao() throws Exception {
        BaseConhecimentoCadastroInput input = new BaseConhecimentoCadastroInput();

        mockMvc.perform(post("/v1/faq/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(baseConhecimentoService);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve retornar not found quando tentar atualizar item inexistente")
    void deveRetornarNotFoundQuandoTentarAtualizarItemInexistente() throws Exception {
        BaseConhecimentoEdicaoInput input = new BaseConhecimentoEdicaoInput();
        input.setTitulo("FAQ Atualizada");
        input.setConteudo("Conteúdo atualizado");

        when(baseConhecimentoService.buscarPorIdOuFalhar(99L))
                .thenThrow(new BaseConhecimentoNaoEncontradaException(99L));

        mockMvc.perform(put("/v1/faq/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(baseConhecimentoService).buscarPorIdOuFalhar(99L);
    }
}