package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.AuthInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.AuthTokenOutput;
import br.ufma.glp.unidesk.backend.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthTokenOutput> login(@RequestBody AuthInput authInput) {
        String token = authService.login(authInput.getUsuario(), authInput.getSenha());
        return ResponseEntity.ok(new AuthTokenOutput(token));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        authService.logout();
        //TODO: Implementar a lógica de logout pois nao está funcionando
        return ResponseEntity.ok("Logout realizado com sucesso");
    }
}
