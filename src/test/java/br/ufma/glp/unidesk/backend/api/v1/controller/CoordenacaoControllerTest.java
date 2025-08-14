package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenacaoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenacaoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenacaoModel;
import br.ufma.glp.unidesk.backend.domain.exception.CoordenacaoNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.service.CoordenacaoService;
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

@WebMvcTest(controllers = CoordenacaoController.class)
class CoordenacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CoordenacaoService coordenacaoService;

    @MockBean
    private CoordenacaoModelAssembler coordenacaoModelAssembler;

    @MockBean
    private CoordenacaoCadastroInputDisassembler coordenacaoCadastroInputDisassembler;

    @MockBean
    private CoordenacaoEdicaoInputDisassembler coordenacaoEdicaoInputDisassembler;

    @Test
    @DisplayName("Deve listar todas as coordenações para qualquer usuário")
    @WithMockUser
    void deveListarTodasCoordenacoesParaQualquerUsuario() throws Exception {
        CoordenacaoModel model = new CoordenacaoModel();
        model.setIdCoordenacao(1L);
        model.setNome("Coordenação Teste");

        when(coordenacaoService.listarTodas()).thenReturn(List.of(new Coordenacao()));
        when(coordenacaoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/coordenacoes/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCoordenacao").value(1))
                .andExpect(jsonPath("$[0].nome").value("Coordenação Teste"));

        verify(coordenacaoService).listarTodas();
        verify(coordenacaoModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando não autenticado")
    void deveRetornarUnauthorizedQuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/v1/coordenacoes/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(coordenacaoService);
        verifyNoInteractions(coordenacaoModelAssembler);
    }

    @Test
    @DisplayName("Deve buscar coordenação por ID quando existir")
    @WithMockUser
    void deveBuscarCoordenacaoPorIdQuandoExistir() throws Exception {
        CoordenacaoModel model = new CoordenacaoModel();
        model.setIdCoordenacao(1L);
        model.setNome("Coordenação Teste");

        when(coordenacaoService.buscarPorIdOuFalhar(1L)).thenReturn(new Coordenacao());
        when(coordenacaoModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/coordenacoes/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCoordenacao").value(1))
                .andExpect(jsonPath("$.nome").value("Coordenação Teste"));

        verify(coordenacaoService).buscarPorIdOuFalhar(1L);
        verify(coordenacaoModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar por ID inexistente")
    @WithMockUser
    void deveRetornarNotFoundQuandoBuscarPorIdInexistente() throws Exception {
        when(coordenacaoService.buscarPorIdOuFalhar(99L))
                .thenThrow(new CoordenacaoNaoEncontradaException(99L));

        mockMvc.perform(get("/v1/coordenacoes/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(coordenacaoService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @DisplayName("Deve buscar coordenações por nome quando existirem")
    @WithMockUser
    void deveBuscarCoordenacoesPorNomeQuandoExistirem() throws Exception {
        CoordenacaoModel model = new CoordenacaoModel();
        model.setIdCoordenacao(1L);
        model.setNome("Coordenação Computação");

        when(coordenacaoService.buscarPorNome("Computação")).thenReturn(List.of(new Coordenacao()));
        when(coordenacaoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/coordenacoes/buscar")
                        .param("nome", "Computação")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCoordenacao").value(1))
                .andExpect(jsonPath("$[0].nome").value("Coordenação Computação"));

        verify(coordenacaoService).buscarPorNome("Computação");
        verify(coordenacaoModelAssembler).toCollectionModel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve criar coordenação quando ADMIN autorizado")
    void deveCriarCoordenacaoQuandoAdminAutorizado() throws Exception {
        CoordenacaoCadastroInput input = new CoordenacaoCadastroInput();
        input.setNome("Nova Coordenação");
        input.setIdCurso(1L);

        Coordenacao domain = new Coordenacao();
        CoordenacaoModel model = new CoordenacaoModel();
        model.setIdCoordenacao(1L);
        model.setNome("Nova Coordenação");

        when(coordenacaoCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(coordenacaoService.salvar(domain)).thenReturn(domain);
        when(coordenacaoModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/coordenacoes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCoordenacao").value(1))
                .andExpect(jsonPath("$.nome").value("Nova Coordenação"));

        verify(coordenacaoCadastroInputDisassembler).toDomainObject(any());
        verify(coordenacaoService).salvar(domain);
        verify(coordenacaoModelAssembler).toModel(domain);
    }


    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve atualizar coordenação quando COORDENADOR autorizado")
    void deveAtualizarCoordenacaoQuandoCoordenadorAutorizado() throws Exception {
        CoordenacaoEdicaoInput input = new CoordenacaoEdicaoInput();
        input.setNome("Coordenação Atualizada");

        Coordenacao existente = new Coordenacao();
        Coordenacao atualizada = new Coordenacao();
        CoordenacaoModel model = new CoordenacaoModel();
        model.setIdCoordenacao(1L);
        model.setNome("Coordenação Atualizada");

        when(coordenacaoService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(coordenacaoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(coordenacaoService.atualizar(existente)).thenReturn(atualizada);
        when(coordenacaoModelAssembler.toModel(atualizada)).thenReturn(model);

        mockMvc.perform(put("/v1/coordenacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCoordenacao").value(1))
                .andExpect(jsonPath("$.nome").value("Coordenação Atualizada"));

        verify(coordenacaoService).buscarPorIdOuFalhar(1L);
        verify(coordenacaoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(coordenacaoService).atualizar(existente);
        verify(coordenacaoModelAssembler).toModel(atualizada);
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    @DisplayName("Deve atualizar coordenação quando FUNCIONARIO_COORDENACAO autorizado")
    void deveAtualizarCoordenacaoQuandoFuncionarioCoordenacaoAutorizado() throws Exception {
        CoordenacaoEdicaoInput input = new CoordenacaoEdicaoInput();
        input.setNome("Coordenação Atualizada");

        Coordenacao existente = new Coordenacao();
        Coordenacao atualizada = new Coordenacao();
        CoordenacaoModel model = new CoordenacaoModel();
        model.setIdCoordenacao(1L);
        model.setNome("Coordenação Atualizada");

        when(coordenacaoService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(coordenacaoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(coordenacaoService.atualizar(existente)).thenReturn(atualizada);
        when(coordenacaoModelAssembler.toModel(atualizada)).thenReturn(model);

        mockMvc.perform(put("/v1/coordenacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCoordenacao").value(1))
                .andExpect(jsonPath("$.nome").value("Coordenação Atualizada"));

        verify(coordenacaoService).buscarPorIdOuFalhar(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve remover coordenação quando ADMIN autorizado")
    void deveRemoverCoordenacaoQuandoAdminAutorizado() throws Exception {
        doNothing().when(coordenacaoService).excluir(1L);

        mockMvc.perform(delete("/v1/coordenacoes/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(coordenacaoService).excluir(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar bad request quando dados inválidos no cadastro")
    void deveRetornarBadRequestQuandoDadosInvalidosNoCadastro() throws Exception {
        CoordenacaoCadastroInput input = new CoordenacaoCadastroInput();

        mockMvc.perform(post("/v1/coordenacoes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(coordenacaoService);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve retornar not found quando tentar atualizar coordenação inexistente")
    void deveRetornarNotFoundQuandoTentarAtualizarCoordenacaoInexistente() throws Exception {
        CoordenacaoEdicaoInput input = new CoordenacaoEdicaoInput();
        input.setNome("Coordenação Atualizada");

        when(coordenacaoService.buscarPorIdOuFalhar(99L))
                .thenThrow(new CoordenacaoNaoEncontradaException(99L));

        mockMvc.perform(put("/v1/coordenacoes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(coordenacaoService).buscarPorIdOuFalhar(99L);
    }
}