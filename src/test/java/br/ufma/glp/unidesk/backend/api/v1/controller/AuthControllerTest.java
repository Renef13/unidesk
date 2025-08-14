
package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.config.JwtUtil;
import br.ufma.glp.unidesk.backend.api.v1.assembler.UsuarioModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.AuthInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.UsuarioModel;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.service.AuthService;
import br.ufma.glp.unidesk.backend.domain.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioModelAssembler usuarioModelAssembler;

    private AuthInput authInput;
    private Usuario usuario;
    private UsuarioModel usuarioModel;

    @MockBean
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    void setUp() {
        authInput = new AuthInput();
        authInput.setUsuario("testuser");
        authInput.setSenha("password");

        usuario = new Coordenador();
        usuario.setIdUsuario(1L);
        usuario.setUsuario("testuser");

        usuarioModel = new UsuarioModel();
        usuarioModel.setIdUsuario(1L);
        token = "valid-token";
        when(jwtUtil.validateToken(anyString())).thenReturn("testuser");

        var authentication = new UsernamePasswordAuthenticationToken(
                "testuser", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void loginComCredenciaisValidasDeveRetornarToken() throws Exception {
        when(authService.login(anyString(), anyString())).thenReturn("valid-token");

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authInput))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("valid-token"));

        verify(authService).login(authInput.getUsuario(), authInput.getSenha());
    }

    @Test
    void loginComCredenciaisInvalidasDeveTratarExcecao() throws Exception {
        when(authService.login(anyString(), anyString())).thenThrow(new RuntimeException("Credenciais inválidas"));

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authInput))
                        .with(csrf())  // 👈 ADICIONE AQUI
                )
                .andExpect(status().isInternalServerError());

        verify(authService).login(authInput.getUsuario(), authInput.getSenha());
    }


    @Test
    void logoutDeveRetornarMensagemDeSucesso() throws Exception {
        doNothing().when(authService).logout();

        mockMvc.perform(post("/v1/auth/logout")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .with(request -> {
                            request.setUserPrincipal(() -> "testuser");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout realizado com sucesso"));

        verify(authService).logout();
    }

    @Test
    void meComUsuarioAutenticadoDeveRetornarDadosDoUsuario() throws Exception {
        when(authService.getCurrentUsuario()).thenReturn("testuser");
        when(usuarioService.buscarPorUsuario("testuser")).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(usuarioModel);

        mockMvc.perform(get("/v1/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1));

        verify(authService).getCurrentUsuario();
        verify(usuarioService).buscarPorUsuario("testuser");
        verify(usuarioModelAssembler).toModel(usuario);
    }

    @Test
    void meSemUsuarioAutenticadoDeveRetornarNaoAutorizado() throws Exception {
        when(authService.getCurrentUsuario()).thenReturn(null);

        SecurityContextHolder.clearContext();

        mockMvc.perform(get("/v1/auth/me"))
                .andExpect(status().isUnauthorized());

        verify(authService).getCurrentUsuario();
        verifyNoInteractions(usuarioService);
        verifyNoInteractions(usuarioModelAssembler);
    }
}