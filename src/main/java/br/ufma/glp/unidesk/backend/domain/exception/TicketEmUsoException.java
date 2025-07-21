package br.ufma.glp.unidesk.backend.domain.exception;

public class TicketEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public TicketEmUsoException(String mensagem) {
        super(mensagem);
    }

    public TicketEmUsoException(Long idTicket) {
        this(String.format("Ticket de código %d não pode ser removido, pois está em uso", idTicket));
    }
}