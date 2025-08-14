package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.RelatorioCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.RelatorioEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.RelatorioModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.RelatorioCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.RelatorioEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.RelatorioModel;
import br.ufma.glp.unidesk.backend.domain.exception.RelatorioNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Relatorio;
import br.ufma.glp.unidesk.backend.domain.service.RelatorioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RelatorioController.class)
class RelatorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RelatorioService relatorioService;

    @MockBean
    private RelatorioModelAssembler relatorioModelAssembler;

    @MockBean
    private RelatorioCadastroInputDisassembler relatorioCadastroInputDisassembler;

    @MockBean
    private RelatorioEdicaoInputDisassembler relatorioEdicaoInputDisassembler;

    @Test
    @DisplayName("Deve listar todos os relatórios quando ADMIN está autenticado")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosRelatoriosQuandoAdminAutenticado() throws Exception {
        RelatorioModel model = new RelatorioModel();
        model.setIdRelatorio(1L);
        model.setConteudo("Conteúdo do relatório teste");
        model.setData(LocalDate.now());
        model.setTipoRelatorio("Mensal");

        when(relatorioService.listarTodos()).thenReturn(List.of(new Relatorio()));
        when(relatorioModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/relatorios/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRelatorio").value(1))
                .andExpect(jsonPath("$[0].conteudo").value("Conteúdo do relatório teste"))
                .andExpect(jsonPath("$[0].tipoRelatorio").value("Mensal"));

        verify(relatorioService).listarTodos();
        verify(relatorioModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando não autenticado tenta listar relatórios")
    void deveRetornarUnauthorizedQuandoNaoAutenticadoTentaListar() throws Exception {
        mockMvc.perform(get("/v1/relatorios/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(relatorioService);
        verifyNoInteractions(relatorioModelAssembler);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve buscar relatório por ID quando existir")
    void deveBuscarRelatorioPorIdQuandoExistir() throws Exception {
        RelatorioModel model = new RelatorioModel();
        model.setIdRelatorio(1L);
        model.setConteudo("Conteúdo do relatório");
        model.setData(LocalDate.now());
        model.setTipoRelatorio("Mensal");

        when(relatorioService.buscarPorIdOuFalhar(1L)).thenReturn(new Relatorio());
        when(relatorioModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/relatorios/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRelatorio").value(1))
                .andExpect(jsonPath("$.conteudo").value("Conteúdo do relatório"))
                .andExpect(jsonPath("$.tipoRelatorio").value("Mensal"));

        verify(relatorioService).buscarPorIdOuFalhar(1L);
        verify(relatorioModelAssembler).toModel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar not found quando buscar relatório por ID inexistente")
    void deveRetornarNotFoundQuandoBuscarRelatorioPorIdInexistente() throws Exception {
        when(relatorioService.buscarPorIdOuFalhar(99L))
                .thenThrow(new RelatorioNaoEncontradoException(99L));

        mockMvc.perform(get("/v1/relatorios/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(relatorioService).buscarPorIdOuFalhar(99L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve adicionar relatório quando ADMIN está autenticado")
    void deveAdicionarRelatorioQuandoAdminAutenticado() throws Exception {
        RelatorioCadastroInput input = new RelatorioCadastroInput();
        input.setConteudo("Conteúdo do novo relatório");
        input.setData(LocalDate.now());
        input.setTipoRelatorio("Trimestral");

        Relatorio domain = new Relatorio();
        RelatorioModel model = new RelatorioModel();
        model.setIdRelatorio(1L);
        model.setConteudo("Conteúdo do novo relatório");
        model.setTipoRelatorio("Trimestral");

        when(relatorioCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(relatorioService.salvar(domain)).thenReturn(domain);
        when(relatorioModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/relatorios/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRelatorio").value(1))
                .andExpect(jsonPath("$.conteudo").value("Conteúdo do novo relatório"))
                .andExpect(jsonPath("$.tipoRelatorio").value("Trimestral"));

        verify(relatorioCadastroInputDisassembler).toDomainObject(any());
        verify(relatorioService).salvar(domain);
        verify(relatorioModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve adicionar relatório quando COORDENADOR está autenticado")
    void deveAdicionarRelatorioQuandoCoordenadorAutenticado() throws Exception {
        RelatorioCadastroInput input = new RelatorioCadastroInput();
        input.setConteudo("Conteúdo do novo relatório");
        input.setData(LocalDate.now());
        input.setTipoRelatorio("Trimestral");

        Relatorio domain = new Relatorio();
        RelatorioModel model = new RelatorioModel();
        model.setIdRelatorio(1L);

        when(relatorioCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(relatorioService.salvar(domain)).thenReturn(domain);
        when(relatorioModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/relatorios/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRelatorio").value(1));

        verify(relatorioCadastroInputDisassembler).toDomainObject(any());
        verify(relatorioService).salvar(domain);
        verify(relatorioModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve atualizar relatório quando ADMIN está autenticado")
    void deveAtualizarRelatorioQuandoAdminAutenticado() throws Exception {
        RelatorioEdicaoInput input = new RelatorioEdicaoInput();
        input.setConteudo("Conteúdo atualizado");
        input.setData(LocalDate.now());
        input.setTipoRelatorio("Semestral");

        Relatorio existente = new Relatorio();
        Relatorio atualizado = new Relatorio();
        RelatorioModel model = new RelatorioModel();
        model.setIdRelatorio(1L);
        model.setConteudo("Conteúdo atualizado");
        model.setTipoRelatorio("Semestral");

        when(relatorioService.buscarPorIdOuFalhar(1L)).thenReturn(existente);
        doNothing().when(relatorioEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(relatorioService.atualizar(existente)).thenReturn(atualizado);
        when(relatorioModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/relatorios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRelatorio").value(1))
                .andExpect(jsonPath("$.conteudo").value("Conteúdo atualizado"))
                .andExpect(jsonPath("$.tipoRelatorio").value("Semestral"));

        verify(relatorioService).buscarPorIdOuFalhar(1L);
        verify(relatorioEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(relatorioService).atualizar(existente);
        verify(relatorioModelAssembler).toModel(atualizado);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve remover relatório quando ADMIN está autenticado")
    void deveRemoverRelatorioQuandoAdminAutenticado() throws Exception {
        doNothing().when(relatorioService).remover(1L);

        mockMvc.perform(delete("/v1/relatorios/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(relatorioService).remover(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar bad request quando dados inválidos na criação de relatório")
    void deveRetornarBadRequestQuandoDadosInvalidosNaCriacao() throws Exception {
        RelatorioCadastroInput input = new RelatorioCadastroInput();

        mockMvc.perform(post("/v1/relatorios/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(relatorioService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar not found quando tentar atualizar relatório inexistente")
    void deveRetornarNotFoundQuandoTentarAtualizarRelatorioInexistente() throws Exception {
        RelatorioEdicaoInput input = new RelatorioEdicaoInput();
        input.setConteudo("Conteúdo atualizado");
        input.setData(LocalDate.now());
        input.setTipoRelatorio("Semestral");

        when(relatorioService.buscarPorIdOuFalhar(99L))
                .thenThrow(new RelatorioNaoEncontradoException(99L));

        mockMvc.perform(put("/v1/relatorios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(relatorioService).buscarPorIdOuFalhar(99L);
    }
}