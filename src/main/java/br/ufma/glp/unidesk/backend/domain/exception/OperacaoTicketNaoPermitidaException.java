package br.ufma.glp.unidesk.backend.domain.exception;

public class OperacaoTicketNaoPermitidaException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public OperacaoTicketNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}