package br.ufma.glp.unidesk.backend.domain.exception;

public class EmailJaCadastradoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public EmailJaCadastradoException(String email) {
        super(String.format("Já existe um usuário cadastrado com o email %s", email));
    }
}