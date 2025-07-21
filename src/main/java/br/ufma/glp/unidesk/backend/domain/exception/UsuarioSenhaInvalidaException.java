package br.ufma.glp.unidesk.backend.domain.exception;

public class UsuarioSenhaInvalidaException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public UsuarioSenhaInvalidaException() {
        super("A senha informada não corresponde à senha do usuário");
    }
}