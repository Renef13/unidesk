package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.FuncionarioCoordenacaoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.FuncionarioCoordenacaoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.FuncionarioCoordenacaoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.FuncionarioCoordenacaoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.FuncionarioCoordenacaoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenacaoModel;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.FuncionarioCoordenacaoModel;
import br.ufma.glp.unidesk.backend.domain.exception.FuncionarioCoordenacaoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.service.AuthService;
import br.ufma.glp.unidesk.backend.domain.service.FuncionarioCoordenacaoService;
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

@WebMvcTest(controllers = FuncionarioCoordenacaoController.class)
class FuncionarioCoordenacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FuncionarioCoordenacaoService funcionarioCoordenacaoService;

    @MockBean
    private FuncionarioCoordenacaoModelAssembler funcionarioCoordenacaoModelAssembler;

    @MockBean
    private FuncionarioCoordenacaoCadastroInputDisassembler funcionarioCoordenacaoCadastroInputDisassembler;

    @MockBean
    private FuncionarioCoordenacaoEdicaoInputDisassembler funcionarioCoordenacaoEdicaoInputDisassembler;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("Deve listar todos os funcionários quando usuário autenticado")
    @WithMockUser
    void deveListarTodosFuncionariosQuandoUsuarioAutenticado() throws Exception {
        Usuario usuarioAutenticado = new FuncionarioCoordenacao();
        FuncionarioCoordenacaoModel model = new FuncionarioCoordenacaoModel();
        model.setIdUsuario(1L);
        model.setNome("Funcionário Teste");
        model.setMatricula("12345");

        when(authService.getCurrentUsuarioEntity()).thenReturn(usuarioAutenticado);
        when(funcionarioCoordenacaoService.listarTodos(usuarioAutenticado)).thenReturn(List.of(new FuncionarioCoordenacao()));
        when(funcionarioCoordenacaoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/funcionarios-coordenacao/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].nome").value("Funcionário Teste"))
                .andExpect(jsonPath("$[0].matricula").value("12345"));

        verify(authService).getCurrentUsuarioEntity();
        verify(funcionarioCoordenacaoService).listarTodos(usuarioAutenticado);
        verify(funcionarioCoordenacaoModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando não autenticado")
    void deveRetornarUnauthorizedQuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/v1/funcionarios-coordenacao/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(funcionarioCoordenacaoService);
        verifyNoInteractions(funcionarioCoordenacaoModelAssembler);
    }

    @Test
    @DisplayName("Deve buscar funcionário por ID quando existir")
    @WithMockUser
    void deveBuscarFuncionarioPorIdQuandoExistir() throws Exception {
        FuncionarioCoordenacaoModel model = new FuncionarioCoordenacaoModel();
        model.setIdUsuario(1L);
        model.setNome("Funcionário Teste");
        model.setMatricula("12345");
        model.setCoordenacao(new CoordenacaoModel());

        when(funcionarioCoordenacaoService.buscarPorIdOuFalhar(1L)).thenReturn(new FuncionarioCoordenacao());
        when(funcionarioCoordenacaoModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/funcionarios-coordenacao/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Funcionário Teste"))
                .andExpect(jsonPath("$.matricula").value("12345"));

        verify(funcionarioCoordenacaoService).buscarPorIdOuFalhar(1L);
        verify(funcionarioCoordenacaoModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar por ID inexistente")
    @WithMockUser
    void deveRetornarNotFoundQuandoBuscarPorIdInexistente() throws Exception {
        when(funcionarioCoordenacaoService.buscarPorIdOuFalhar(99L))
                .thenThrow(new FuncionarioCoordenacaoNaoEncontradoException(99L));

        mockMvc.perform(get("/v1/funcionarios-coordenacao/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(funcionarioCoordenacaoService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @DisplayName("Deve buscar funcionários por nome quando existirem")
    @WithMockUser
    void deveBuscarFuncionariosPorNomeQuandoExistirem() throws Exception {
        FuncionarioCoordenacaoModel model = new FuncionarioCoordenacaoModel();
        model.setIdUsuario(1L);
        model.setNome("João Silva");

        when(funcionarioCoordenacaoService.buscarPorNome("João")).thenReturn(List.of(new FuncionarioCoordenacao()));
        when(funcionarioCoordenacaoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/funcionarios-coordenacao/buscar")
                        .param("nome", "João")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));

        verify(funcionarioCoordenacaoService).buscarPorNome("João");
        verify(funcionarioCoordenacaoModelAssembler).toCollectionModel(any());
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve adicionar funcionário quando COORDENADOR autorizado")
    void deveAdicionarFuncionarioQuandoCoordenadorAutorizado() throws Exception {
        FuncionarioCoordenacaoCadastroInput input = new FuncionarioCoordenacaoCadastroInput();
        input.setNome("Novo Funcionário");
        input.setEmail("funcionario@teste.com");
        input.setUsuario("funcionario");
        input.setSenha("senha123");
        input.setMatricula("54321");
        input.setIdCoordenacao(1L);

        FuncionarioCoordenacao domain = new FuncionarioCoordenacao();
        Coordenacao coordenacao = new Coordenacao();
        coordenacao.setIdCoordenacao(1L);
        domain.setCoordenacao(coordenacao);

        FuncionarioCoordenacaoModel model = new FuncionarioCoordenacaoModel();
        model.setIdUsuario(1L);
        model.setNome("Novo Funcionário");
        model.setMatricula("54321");

        CoordenacaoModel coordenacaoModel = new CoordenacaoModel();
        coordenacaoModel.setIdCoordenacao(1L);
        model.setCoordenacao(coordenacaoModel);

        when(funcionarioCoordenacaoCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(funcionarioCoordenacaoService.salvar(domain)).thenReturn(domain);
        when(funcionarioCoordenacaoModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/funcionarios-coordenacao/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Funcionário"))
                .andExpect(jsonPath("$.matricula").value("54321"))
                .andExpect(jsonPath("$.coordenacao.idCoordenacao").value(1));

        verify(funcionarioCoordenacaoCadastroInputDisassembler).toDomainObject(any());
        verify(funcionarioCoordenacaoService).salvar(domain);
        verify(funcionarioCoordenacaoModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    @DisplayName("Deve atualizar funcionário quando FUNCIONARIO_COORDENACAO autorizado")
    void deveAtualizarFuncionarioQuandoFuncionarioCoordenacaoAutorizado() throws Exception {
        FuncionarioCoordenacaoEdicaoInput input = new FuncionarioCoordenacaoEdicaoInput();
        input.setNome("Funcionário Atualizado");
        input.setEmail("atualizado@teste.com");

        FuncionarioCoordenacao existente = new FuncionarioCoordenacao();
        FuncionarioCoordenacao atualizado = new FuncionarioCoordenacao();

        FuncionarioCoordenacaoModel model = new FuncionarioCoordenacaoModel();
        model.setIdUsuario(1L);
        model.setNome("Funcionário Atualizado");
        model.setEmail("atualizado@teste.com");

        when(funcionarioCoordenacaoService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(funcionarioCoordenacaoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(funcionarioCoordenacaoService.atualizar(existente)).thenReturn(atualizado);
        when(funcionarioCoordenacaoModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/funcionarios-coordenacao/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Funcionário Atualizado"))
                .andExpect(jsonPath("$.email").value("atualizado@teste.com"));

        verify(funcionarioCoordenacaoService).buscarPorIdOuFalhar(1L);
        verify(funcionarioCoordenacaoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(funcionarioCoordenacaoService).atualizar(existente);
        verify(funcionarioCoordenacaoModelAssembler).toModel(atualizado);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve remover funcionário quando ADMIN autorizado")
    void deveRemoverFuncionarioQuandoAdminAutorizado() throws Exception {
        doNothing().when(funcionarioCoordenacaoService).remover(1L);

        mockMvc.perform(delete("/v1/funcionarios-coordenacao/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(funcionarioCoordenacaoService).remover(1L);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve retornar bad request quando dados inválidos no cadastro")
    void deveRetornarBadRequestQuandoDadosInvalidosNoCadastro() throws Exception {
        FuncionarioCoordenacaoCadastroInput input = new FuncionarioCoordenacaoCadastroInput();

        mockMvc.perform(post("/v1/funcionarios-coordenacao/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(funcionarioCoordenacaoService);
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    @DisplayName("Deve retornar not found quando tentar atualizar funcionário inexistente")
    void deveRetornarNotFoundQuandoTentarAtualizarFuncionarioInexistente() throws Exception {
        FuncionarioCoordenacaoEdicaoInput input = new FuncionarioCoordenacaoEdicaoInput();
        input.setNome("Funcionário Atualizado");

        when(funcionarioCoordenacaoService.buscarPorIdOuFalhar(99L))
                .thenThrow(new FuncionarioCoordenacaoNaoEncontradoException(99L));

        mockMvc.perform(put("/v1/funcionarios-coordenacao/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(funcionarioCoordenacaoService).buscarPorIdOuFalhar(99L);
    }
}