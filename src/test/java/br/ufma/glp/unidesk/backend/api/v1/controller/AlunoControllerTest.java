package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.AlunoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.AlunoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.AlunoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.AlunoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.AlunoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.AlunoModel;
import br.ufma.glp.unidesk.backend.domain.exception.AlunoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.service.AlunoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AlunoController.class)
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlunoService alunoService;

    @MockBean
    private AlunoModelAssembler alunoModelAssembler;

    @MockBean
    private AlunoCadastroInputDisassembler alunoCadastroInputDisassembler;

    @MockBean
    private AlunoEdicaoInputDisassembler alunoEdicaoInputDisassembler;

    @DisplayName("Deve retornar lista de alunos com status 200 quando usuário for ADMIN")
    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarListagemQuandoAdmin() throws Exception {
        AlunoModel model = new AlunoModel();
        model.setIdUsuario(1L);
        model.setNome("Aluno Teste");

        when(alunoService.listarTodos()).thenReturn(List.of(new Aluno()));
        when(alunoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/alunos/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].nome").value("Aluno Teste"));
    }

    @DisplayName("Deve retornar Unauthorized quando não autenticado")
    @Test
    void deveRetornarForbiddenQuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/v1/alunos/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Retorna lista por curso")
    @WithMockUser
    void listarPorCursoAtualDeveRetornarOkQuandoAutenticado() throws Exception {
        AlunoModel model = new AlunoModel();
        model.setIdUsuario(2L);

        when(alunoService.listarPorCursoAtual()).thenReturn(List.of(new Aluno()));
        when(alunoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/alunos/meu-curso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorIdDeveRetornarOkQuandoAdmin() throws Exception {
        AlunoModel model = new AlunoModel();
        model.setIdUsuario(3L);

        when(alunoService.buscarPorIdOuFalhar(3L)).thenReturn(new Aluno());
        when(alunoModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/alunos/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(3));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorNomeDeveRetornarOkQuandoAdmin() throws Exception {
        AlunoModel model = new AlunoModel();
        model.setIdUsuario(4L);

        when(alunoService.buscarPorNomeOuFalhar("João")).thenReturn(new Aluno());
        when(alunoModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/alunos/buscar").param("nome", "João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(4));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adicionarDeveRetornarCreatedQuandoAdmin() throws Exception {
        AlunoCadastroInput input = new AlunoCadastroInput();
        input.setNome("Novo Aluno");
        input.setEmail("email@email.com");
        input.setUsuario("usuario");
        input.setSenha("senha123");
        input.setMatricula("2023001");
        input.setIdCurso(1L);

        Aluno domain = new Aluno();
        AlunoModel model = new AlunoModel();
        model.setIdUsuario(5L);

        when(alunoCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(alunoService.salvar(domain)).thenReturn(domain);
        when(alunoModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/alunos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(5));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "ALUNO"})
    void atualizarDeveRetornarOkQuandoAdminOuAluno() throws Exception {
        AlunoEdicaoInput input = new AlunoEdicaoInput();
        input.setNome("Aluno Editado");
        input.setEmail("email@email.com");
        input.setUsuario("usuario");
        input.setSenha("senha123");
        input.setMatricula("2023001");
        input.setIdCurso(1L);

        Aluno existente = new Aluno();
        Aluno atualizado = new Aluno();
        AlunoModel model = new AlunoModel();
        model.setIdUsuario(6L);

        when(alunoService.buscarPorIdOuFalhar(6L)).thenReturn(existente);
        doNothing().when(alunoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(alunoService.atualizar(existente)).thenReturn(atualizado);
        when(alunoModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/alunos/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(6));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DeveRetornarNoContentQuandoAdmin() throws Exception {
        mockMvc.perform(delete("/v1/alunos/7")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ALUNO")
    void deveRetornarForbiddenQuandoAluno() throws Exception {
        mockMvc.perform(delete("/v1/alunos/7"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ALUNO")
    void deveRetornarForbiddenQuandoAlunoTentaAdicionar() throws Exception {
        AlunoCadastroInput input = new AlunoCadastroInput();
        input.setNome("Novo Aluno");

        mockMvc.perform(post("/v1/alunos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarBadRequestQuandoDadosInvalidosNoAdicionar() throws Exception {
        AlunoCadastroInput input = new AlunoCadastroInput();

        mockMvc.perform(post("/v1/alunos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarNotFoundQuandoAlunoNaoExistir() throws Exception {
        when(alunoService.buscarPorIdOuFalhar(99L)).thenThrow(new AlunoNaoEncontradoException(99L));

        mockMvc.perform(get("/v1/alunos/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarNotFoundQuandoAlunoNaoExistirNaBusca() throws Exception {
        when(alunoService.buscarPorNomeOuFalhar("Inexistente"))
                .thenThrow(new AlunoNaoEncontradoException("Aluno não encontrado: Inexistente"));

        mockMvc.perform(get("/v1/alunos/buscar").param("nome", "Inexistente")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarDadosCompletosQuandoListar() throws Exception {
        Aluno aluno = new Aluno();

        AlunoModel model = new AlunoModel();
        model.setIdUsuario(1L);
        model.setNome("Aluno Teste");
        model.setEmail("aluno@teste.com");
        model.setMatricula("2023001");

        when(alunoService.listarTodos()).thenReturn(List.of(aluno));
        when(alunoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/alunos/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].nome").value("Aluno Teste"))
                .andExpect(jsonPath("$[0].email").value("aluno@teste.com"))
                .andExpect(jsonPath("$[0].matricula").value("2023001"));
    }

}