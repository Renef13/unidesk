package br.ufma.glp.unidesk.backend.domain.exception;

public class MensagemSemTicketException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public MensagemSemTicketException(String mensagem) {
        super(mensagem);
    }

    public MensagemSemTicketException() {
        this("A mensagem deve estar vinculada a um ticket válido");
    }
}