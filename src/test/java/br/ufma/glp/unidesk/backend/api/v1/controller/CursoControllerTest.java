package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CursoModel;
import br.ufma.glp.unidesk.backend.domain.exception.CursoNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import br.ufma.glp.unidesk.backend.domain.service.CursoService;
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

@WebMvcTest(controllers = CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private CursoModelAssembler cursoModelAssembler;

    @MockBean
    private CursoCadastroInputDisassembler cursoCadastroInputDisassembler;

    @MockBean
    private CursoEdicaoInputDisassembler cursoEdicaoInputDisassembler;

    @Test
    @DisplayName("Deve listar todos os cursos para qualquer usuário autenticado")
    @WithMockUser
    void deveListarTodosCursosParaUsuarioAutenticado() throws Exception {
        CursoModel model = new CursoModel();
        model.setIdCurso(1L);
        model.setNome("Ciência da Computação");
        model.setCampus("Campus 1");

        when(cursoService.listarCursos()).thenReturn(List.of(new Curso()));
        when(cursoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/cursos/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCurso").value(1))
                .andExpect(jsonPath("$[0].nome").value("Ciência da Computação"))
                .andExpect(jsonPath("$[0].campus").value("Campus 1"));

        verify(cursoService).listarCursos();
        verify(cursoModelAssembler).toCollectionModel(any());
    }

    @Test
    @DisplayName("Deve retornar Unauthorized quando não autenticado tenta listar cursos")
    void deveRetornarUnauthorizedQuandoNaoAutenticadoTentaListar() throws Exception {
        mockMvc.perform(get("/v1/cursos/"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(cursoService);
        verifyNoInteractions(cursoModelAssembler);
    }

    @Test
    @DisplayName("Deve buscar curso por ID quando existir")
    @WithMockUser
    void deveBuscarCursoPorIdQuandoExistir() throws Exception {
        CursoModel model = new CursoModel();
        model.setIdCurso(1L);
        model.setNome("Engenharia da Computação");
        model.setCampus("Campus 2");

        when(cursoService.buscarCursoPorId(1L)).thenReturn(new Curso());
        when(cursoModelAssembler.toModel(any())).thenReturn(model);

        mockMvc.perform(get("/v1/cursos/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nome").value("Engenharia da Computação"))
                .andExpect(jsonPath("$.campus").value("Campus 2"));

        verify(cursoService).buscarCursoPorId(1L);
        verify(cursoModelAssembler).toModel(any());
    }

    @Test
    @DisplayName("Deve retornar not found quando buscar por ID inexistente")
    @WithMockUser
    void deveRetornarNotFoundQuandoBuscarPorIdInexistente() throws Exception {
        when(cursoService.buscarCursoPorId(99L))
                .thenThrow(new CursoNaoEncontradoException(99L));

        mockMvc.perform(get("/v1/cursos/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(cursoService).buscarCursoPorId(99L);
    }

    @Test
    @DisplayName("Deve buscar cursos por nome quando existirem")
    @WithMockUser
    void deveBuscarCursosPorNomeQuandoExistirem() throws Exception {
        CursoModel model = new CursoModel();
        model.setIdCurso(1L);
        model.setNome("Ciência da Computação");

        when(cursoService.buscarCursoPorNome("Computação")).thenReturn(List.of(new Curso()));
        when(cursoModelAssembler.toCollectionModel(any())).thenReturn(List.of(model));

        mockMvc.perform(get("/v1/cursos/buscar")
                        .param("nome", "Computação")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCurso").value(1))
                .andExpect(jsonPath("$[0].nome").value("Ciência da Computação"));

        verify(cursoService).buscarCursoPorNome("Computação");
        verify(cursoModelAssembler).toCollectionModel(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve criar curso quando ADMIN autorizado")
    void deveCriarCursoQuandoAdminAutorizado() throws Exception {
        CursoCadastroInput input = new CursoCadastroInput();
        input.setNome("Novo Curso");
        input.setCampus("Campus Principal");

        Curso domain = new Curso();
        CursoModel model = new CursoModel();
        model.setIdCurso(1L);
        model.setNome("Novo Curso");
        model.setCampus("Campus Principal");

        when(cursoCadastroInputDisassembler.toDomainObject(any())).thenReturn(domain);
        when(cursoService.criarCurso(domain)).thenReturn(domain);
        when(cursoModelAssembler.toModel(domain)).thenReturn(model);

        mockMvc.perform(post("/v1/cursos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Curso"))
                .andExpect(jsonPath("$.campus").value("Campus Principal"));

        verify(cursoCadastroInputDisassembler).toDomainObject(any());
        verify(cursoService).criarCurso(domain);
        verify(cursoModelAssembler).toModel(domain);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve atualizar curso quando COORDENADOR autorizado")
    void deveAtualizarCursoQuandoCoordenadorAutorizado() throws Exception {
        CursoEdicaoInput input = new CursoEdicaoInput();
        input.setNome("Curso Atualizado");
        input.setCampus("Campus Atualizado");

        Curso existente = new Curso();
        Curso atualizado = new Curso();
        CursoModel model = new CursoModel();
        model.setIdCurso(1L);
        model.setNome("Curso Atualizado");
        model.setCampus("Campus Atualizado");

        when(cursoService.buscarCursoPorId(1L)).thenReturn(existente);
        doNothing().when(cursoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(cursoService.atualizarCurso(existente)).thenReturn(atualizado);
        when(cursoModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nome").value("Curso Atualizado"))
                .andExpect(jsonPath("$.campus").value("Campus Atualizado"));

        verify(cursoService).buscarCursoPorId(1L);
        verify(cursoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(cursoService).atualizarCurso(existente);
        verify(cursoModelAssembler).toModel(atualizado);
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO_COORDENACAO")
    @DisplayName("Deve atualizar curso quando FUNCIONARIO_COORDENACAO autorizado")
    void deveAtualizarCursoQuandoFuncionarioCoordenacaoAutorizado() throws Exception {
        CursoEdicaoInput input = new CursoEdicaoInput();
        input.setNome("Curso Atualizado");
        input.setCampus("Campus Atualizado");

        Curso existente = new Curso();
        Curso atualizado = new Curso();
        CursoModel model = new CursoModel();
        model.setIdCurso(1L);
        model.setNome("Curso Atualizado");

        when(cursoService.buscarCursoPorId(1L)).thenReturn(existente);
        doNothing().when(cursoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        when(cursoService.atualizarCurso(existente)).thenReturn(atualizado);
        when(cursoModelAssembler.toModel(atualizado)).thenReturn(model);

        mockMvc.perform(put("/v1/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nome").value("Curso Atualizado"));

        verify(cursoService).buscarCursoPorId(1L);
        verify(cursoEdicaoInputDisassembler).copyToDomainObject(input, existente);
        verify(cursoService).atualizarCurso(existente);
        verify(cursoModelAssembler).toModel(atualizado);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve remover curso quando ADMIN autorizado")
    void deveRemoverCursoQuandoAdminAutorizado() throws Exception {
        doNothing().when(cursoService).desativarCurso(1L);

        mockMvc.perform(delete("/v1/cursos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(cursoService).desativarCurso(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Deve retornar bad request quando dados inválidos no cadastro")
    void deveRetornarBadRequestQuandoDadosInvalidosNoCadastro() throws Exception {
        CursoCadastroInput input = new CursoCadastroInput();

        mockMvc.perform(post("/v1/cursos/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cursoService);
    }

    @Test
    @WithMockUser(roles = "COORDENADOR")
    @DisplayName("Deve retornar not found quando tentar atualizar curso inexistente")
    void deveRetornarNotFoundQuandoTentarAtualizarCursoInexistente() throws Exception {
        CursoEdicaoInput input = new CursoEdicaoInput();
        input.setNome("Curso Atualizado");
        input.setCampus("Campus Atualizado");

        when(cursoService.buscarCursoPorId(99L))
                .thenThrow(new CursoNaoEncontradoException(99L));

        mockMvc.perform(put("/v1/cursos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(cursoService).buscarCursoPorId(99L);
    }
}