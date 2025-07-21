package br.ufma.glp.unidesk.backend.domain.exception;

public class MensagemSemUsuarioException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public MensagemSemUsuarioException(String mensagem) {
        super(mensagem);
    }

    public MensagemSemUsuarioException() {
        this("A mensagem deve estar vinculada a um usuário válido");
    }
}