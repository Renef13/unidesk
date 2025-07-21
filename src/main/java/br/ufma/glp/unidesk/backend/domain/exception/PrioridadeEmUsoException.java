package br.ufma.glp.unidesk.backend.domain.exception;

public class PrioridadeEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public PrioridadeEmUsoException(String mensagem) {
        super(mensagem);
    }

    public PrioridadeEmUsoException(Long idPrioridade) {
        this(String.format("Prioridade de código %d não pode ser removida, pois está em uso", idPrioridade));
    }
}