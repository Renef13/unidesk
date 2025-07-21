package br.ufma.glp.unidesk.backend.domain.exception;

public class TicketNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public TicketNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public TicketNaoEncontradoException(Long idTicket) {
        this(String.format("Não existe um ticket cadastrado com o código %d", idTicket));
    }
}