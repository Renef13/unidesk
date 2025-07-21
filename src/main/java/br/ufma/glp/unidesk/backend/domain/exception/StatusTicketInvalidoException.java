package br.ufma.glp.unidesk.backend.domain.exception;

public class StatusTicketInvalidoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public StatusTicketInvalidoException(String mensagem) {
        super(mensagem);
    }
    
    public StatusTicketInvalidoException(String statusAtual, String statusDesejado) {
        this(String.format("Não é possível alterar o status do ticket de '%s' para '%s'", statusAtual, statusDesejado));
    }
}