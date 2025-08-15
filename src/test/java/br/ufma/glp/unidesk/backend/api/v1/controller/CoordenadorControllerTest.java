package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenadorCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenadorEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenadorModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenadorCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenadorEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenadorModel;
import br.ufma.glp.unidesk.backend.domain.exception.CoordenadorNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import br.ufma.glp.unidesk.backend.domain.service.CoordenadorService;
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

@WebMvcTest(controllers = CoordenadorController.class)
class CoordenadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CoordenadorService coordenadorService;

    @MockBean
    private CoordenadorModelAssembler coordenadorModelAssembler;

    @MockBean
    private CoordenadorCadastroInputDisassembler coordenadorCadastroInputDisassembler;

    @MockBean
    private CoordenadorEdicaoInputDisassembler coordenadorEdicaoInputDisassembler;

    @Test
    @DisplayName("Deve listar todos os coordenadores para qualquer usuário autenticado")
    @WithMockUser
    void deveListarTodosCoordenadoresParaQualquerUsuarioAutenticado() throws Exception {
        CoordenadorModel model = new CoordenadorModel();
        model.setIdUsuario(1L);
        model.setNome("Coordenador Teste");

        when(coordenadorService.listarTodos()).thenReturn(List.of(new Coordenador()));
        when(coordenadorModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/coordenadores/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].nome").value("Coordenador Teste"));

        verify(coordenadorService).listarTodos();
        verify(coordenadorModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando não autenticado tenta listar")
    void deveRetornarUnauthorizedQuandoNaoAutenticadoTentaListar() throws Exception {
        mockMvc.perform(get("/v1/coordenadores/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(coordenadorService);
        verifyNoInteractions(coordenadorModelAssembler);
    }

    @Test
    @DisplayName("Deve buscar coordenador por ID quando existir")
    @WithMockUser
    void deveBuscarCoordenadorPorIdQuandoExistir() throws Exception {
        CoordenadorModel model = new CoordenadorModel();
        model.setIdUsuario(1L);
        model.setNome("Coordenador Teste");

        when(coordenadorService.buscarPorIdOuFalhar(1L)).thenReturn(new Coordenador());
        when(coordenadorModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/coordenadores/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Coordenador Teste"));

        verify(coordenadorService).buscarPorIdOuFalhar(1L);
        verify(coordenadorModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar por ID inexistente")
    @WithMockUser
    void deveRetornarNotFoundQuandoBuscarPorIdInexistente() throws Exception {
        when(coordenadorService.buscarPorIdOuFalhar(99L))
                .thenThrow(new CoordenadorNaoEncontradoException(99L));

        mockMvc.perform(get("/v1/coordenadores/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(coordenadorService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve adicionar coordenador quando ADMIN autorizado")
    void deveAdicionarCoordenadorQuandoAdminAutorizado() throws Exception {
        CoordenadorCadastroInput input = new CoordenadorCadastroInput();
        input.setNome("Novo Coordenador");
        input.setEmail("coordenador@teste.com");
        input.setUsuario("coordenador");
        input.setSenha("senha123");
        input.setIdCoordenacao(1L);
        input.setMatricula("123456");
        input.setAtivo(true);

        Coordenador domain = new Coordenador();
        CoordenadorModel model = new CoordenadorModel();
        model.setIdUsuario(1L);
        model.setNome("Novo Coordenador");

        when(coordenadorCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(coordenadorService.salvar(domain)).thenReturn(domain);
        when(coordenadorModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/coordenadores/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Coordenador"));

        verify(coordenadorCadastroInputDisassembler).toDomainObject(any());
        verify(coordenadorService).salvar(domain);
        verify(coordenadorModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    @DisplayName("Deve adicionar coordenador quando FUNCIONARIO_COORDENACAO autorizado")
    void deveAdicionarCoordenadorQuandoFuncionarioCoordenacaoAutorizado() throws Exception {
        CoordenadorCadastroInput input = new CoordenadorCadastroInput();
        input.setNome("Novo Coordenador");
        input.setEmail("coordenador@teste.com");
        input.setUsuario("coordenador");
        input.setSenha("senha123");
        input.setIdCoordenacao(1L);
        input.setMatricula("123456");
        input.setAtivo(true);

        Coordenador domain = new Coordenador();
        CoordenadorModel model = new CoordenadorModel();
        model.setIdUsuario(1L);
        model.setNome("Novo Coordenador");

        when(coordenadorCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(coordenadorService.salvar(domain)).thenReturn(domain);
        when(coordenadorModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/coordenadores/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Coordenador"));

        verify(coordenadorCadastroInputDisassembler).toDomainObject(any());
        verify(coordenadorService).salvar(domain);
        verify(coordenadorModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve atualizar coordenador quando COORDENADOR autorizado")
    void deveAtualizarCoordenadorQuandoCoordenadorAutorizado() throws Exception {
        CoordenadorEdicaoInput input = new CoordenadorEdicaoInput();
        input.setNome("Coordenador Atualizado");
        input.setEmail("atualizado@teste.com");

        Coordenador existente = new Coordenador();
        Coordenador atualizado = new Coordenador();
        CoordenadorModel model = new CoordenadorModel();
        model.setIdUsuario(1L);
        model.setNome("Coordenador Atualizado");

        when(coordenadorService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(coordenadorEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(coordenadorService.atualizar(existente)).thenReturn(atualizado);
        when(coordenadorModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/coordenadores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Coordenador Atualizado"));

        verify(coordenadorService).buscarPorIdOuFalhar(1L);
        verify(coordenadorEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(coordenadorService).atualizar(existente);
        verify(coordenadorModelAssembler).toModel(atualizado);
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    @DisplayName("Deve atualizar coordenador quando FUNCIONARIO_COORDENACAO autorizado")
    void deveAtualizarCoordenadorQuandoFuncionarioCoordenacaoAutorizado() throws Exception {
        CoordenadorEdicaoInput input = new CoordenadorEdicaoInput();
        input.setNome("Coordenador Atualizado");
        input.setEmail("atualizado@teste.com");

        Coordenador existente = new Coordenador();
        Coordenador atualizado = new Coordenador();
        CoordenadorModel model = new CoordenadorModel();
        model.setIdUsuario(1L);
        model.setNome("Coordenador Atualizado");

        when(coordenadorService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(coordenadorEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(coordenadorService.atualizar(existente)).thenReturn(atualizado);
        when(coordenadorModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/coordenadores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Coordenador Atualizado"));

        verify(coordenadorService).buscarPorIdOuFalhar(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve remover coordenador quando ADMIN autorizado")
    void deveRemoverCoordenadorQuandoAdminAutorizado() throws Exception {
        doNothing().when(coordenadorService).excluir(1L);

        mockMvc.perform(delete("/v1/coordenadores/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(coordenadorService).excluir(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar bad request quando dados inválidos no cadastro")
    void deveRetornarBadRequestQuandoDadosInvalidosNoCadastro() throws Exception {
        CoordenadorCadastroInput input = new CoordenadorCadastroInput();

        mockMvc.perform(post("/v1/coordenadores/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(coordenadorService);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve retornar not found quando tentar atualizar coordenador inexistente")
    void deveRetornarNotFoundQuandoTentarAtualizarCoordenadorInexistente() throws Exception {
        CoordenadorEdicaoInput input = new CoordenadorEdicaoInput();
        input.setNome("Coordenador Atualizado");

        when(coordenadorService.buscarPorIdOuFalhar(99L))
                .thenThrow(new CoordenadorNaoEncontradoException(99L));

        mockMvc.perform(put("/v1/coordenadores/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(coordenadorService).buscarPorIdOuFalhar(99L);
    }
}