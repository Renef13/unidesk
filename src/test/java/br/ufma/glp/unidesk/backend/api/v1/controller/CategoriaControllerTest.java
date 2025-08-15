package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CategoriaCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CategoriaEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CategoriaModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CategoriaCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CategoriaEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CategoriaModel;
import br.ufma.glp.unidesk.backend.domain.exception.CategoriaNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import br.ufma.glp.unidesk.backend.domain.service.CategoriaService;
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

@WebMvcTest(controllers = CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private CategoriaModelAssembler categoriaModelAssembler;

    @MockBean
    private CategoriaCadastroInputDisassembler categoriaCadastroInputDisassembler;

    @MockBean
    private CategoriaEdicaoInputDisassembler categoriaEdicaoInputDisassembler;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve listar todas as categorias quando ADMIN está autenticado")
    void deveListarTodasCategoriasQuandoAdminAutenticado() throws Exception {
        CategoriaModel model = new CategoriaModel();
        model.setIdCategoria(1L);
        model.setNome("Categoria Teste");

        when(categoriaService.listarTodos()).thenReturn(List.of(new Categoria()));
        when(categoriaModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/categorias/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCategoria").value(1))
                .andExpect(jsonPath("$[0].nome").value("Categoria Teste"));

        verify(categoriaService).listarTodos();
        verify(categoriaModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando não autenticado tenta listar")
    void deveRetornarUnauthorizedQuandoNaoAutenticadoTentaListar() throws Exception {
        mockMvc.perform(get("/v1/categorias/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(categoriaService);
        verifyNoInteractions(categoriaModelAssembler);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve buscar categoria por nome quando ADMIN está autenticado")
    void deveBuscarCategoriaPorNomeQuandoAdminAutenticado() throws Exception {
        CategoriaModel model = new CategoriaModel();
        model.setIdCategoria(1L);
        model.setNome("Categoria Teste");

        when(categoriaService.buscarPorNomeContendo("Teste")).thenReturn(List.of(new Categoria()));
        when(categoriaModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/categorias/buscar")
                        .param("nome", "Teste")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCategoria").value(1))
                .andExpect(jsonPath("$[0].nome").value("Categoria Teste"));

        verify(categoriaService).buscarPorNomeContendo("Teste");
        verify(categoriaModelAssembler).toCollectionModel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve buscar categoria por ID quando ADMIN está autenticado")
    void deveBuscarCategoriaPorIdQuandoAdminAutenticado() throws Exception {
        CategoriaModel model = new CategoriaModel();
        model.setIdCategoria(1L);
        model.setNome("Categoria Teste");

        when(categoriaService.buscarPorIdOuFalhar(1L)).thenReturn(new Categoria());
        when(categoriaModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/categorias/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(1))
                .andExpect(jsonPath("$.nome").value("Categoria Teste"));

        verify(categoriaService).buscarPorIdOuFalhar(1L);
        verify(categoriaModelAssembler).toModel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar not found quando buscar categoria por ID inexistente")
    void deveRetornarNotFoundQuandoBuscarCategoriaPorIdInexistente() throws Exception {
        when(categoriaService.buscarPorIdOuFalhar(99L))
                .thenThrow(new CategoriaNaoEncontradaException(99L));

        mockMvc.perform(get("/v1/categorias/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(categoriaService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve adicionar categoria quando ADMIN está autenticado")
    void deveAdicionarCategoriaQuandoAdminAutenticado() throws Exception {
        CategoriaCadastroInput input = new CategoriaCadastroInput();
        input.setNome("Nova Categoria");

        Categoria domain = new Categoria();
        CategoriaModel model = new CategoriaModel();
        model.setIdCategoria(1L);
        model.setNome("Nova Categoria");

        when(categoriaCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(categoriaService.salvar(domain)).thenReturn(domain);
        when(categoriaModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/categorias/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCategoria").value(1))
                .andExpect(jsonPath("$.nome").value("Nova Categoria"));

        verify(categoriaCadastroInputDisassembler).toDomainObject(any());
        verify(categoriaService).salvar(domain);
        verify(categoriaModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve atualizar categoria quando ADMIN está autenticado")
    void deveAtualizarCategoriaQuandoAdminAutenticado() throws Exception {
        CategoriaEdicaoInput input = new CategoriaEdicaoInput();
        input.setNome("Categoria Atualizada");

        Categoria existente = new Categoria();
        Categoria atualizada = new Categoria();
        CategoriaModel model = new CategoriaModel();
        model.setIdCategoria(1L);
        model.setNome("Categoria Atualizada");

        when(categoriaService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(categoriaEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(categoriaService.atualizar(existente)).thenReturn(atualizada);
        when(categoriaModelAssembler.toModel(atualizada)).thenReturn(model);

        mockMvc.perform(put("/v1/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(1))
                .andExpect(jsonPath("$.nome").value("Categoria Atualizada"));

        verify(categoriaService).buscarPorIdOuFalhar(1L);
        verify(categoriaEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(categoriaService).atualizar(existente);
        verify(categoriaModelAssembler).toModel(atualizada);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve remover categoria quando ADMIN está autenticado")
    void deveRemoverCategoriaQuandoAdminAutenticado() throws Exception {
        doNothing().when(categoriaService).excluir(1L);

        mockMvc.perform(delete("/v1/categorias/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoriaService).excluir(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar bad request quando dados inválidos no adicionar")
    void deveRetornarBadRequestQuandoDadosInvalidosNoAdicionar() throws Exception {
        CategoriaCadastroInput input = new CategoriaCadastroInput();

        mockMvc.perform(post("/v1/categorias/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoriaService);
    }

}