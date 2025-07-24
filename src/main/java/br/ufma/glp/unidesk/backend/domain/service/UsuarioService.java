package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.UsuarioSenhaInvalidaException;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.repository.UsuarioRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario buscarPorUsuario(@NotBlank String usuario) {
        return usuarioRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuario));
    }

    public boolean validarCredencias(@NotBlank String usuario, @NotBlank String senha) {
        Optional<Usuario> userOpt = usuarioRepository.findByUsuario(usuario);
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            if (passwordEncoder.matches(senha, user.getSenha())) {
                return true;
            } else {
                throw new UsuarioSenhaInvalidaException();
            }
        }
        return false;
    }

    public void prepararParaSalvar(Usuario usuario) {
        //validarEmailEmUso(usuario.getEmail());
       // validarUsuarioEmUso(usuario.getUsuario());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    }
}