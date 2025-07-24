package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.api.config.JwtUtil;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AuthService {
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public String login(String usuario, String senha) {
        if (usuarioService.validarCredencias(usuario, senha)) {
            Usuario user = usuarioService.buscarPorUsuario(usuario);
            return jwtUtil.generateToken(usuario, user.getRole().getRole());
        }
        throw new IllegalArgumentException("Usuário e/ou senha inválidas");
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
