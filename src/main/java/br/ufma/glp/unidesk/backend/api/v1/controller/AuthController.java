package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.AuthInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.AuthTokenOutput;
import br.ufma.glp.unidesk.backend.domain.service.AuthService;
import br.ufma.glp.unidesk.backend.domain.service.UsuarioService;
import br.ufma.glp.unidesk.backend.api.v1.assembler.UsuarioModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.UsuarioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler usuarioModelAssembler;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthTokenOutput> login(@RequestBody AuthInput authInput) {
        String token = authService.login(authInput.getUsuario(), authInput.getSenha());
        return ResponseEntity.ok(new AuthTokenOutput(token));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UsuarioModel> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String usuarioLogin = auth.getName();
        var usuario = usuarioService.buscarPorUsuario(usuarioLogin);
        UsuarioModel model = usuarioModelAssembler.toModel(usuario);
        return ResponseEntity.ok(model);
    }

}
