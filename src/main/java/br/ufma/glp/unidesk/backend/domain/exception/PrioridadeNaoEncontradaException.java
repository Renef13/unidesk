package br.ufma.glp.unidesk.backend.domain.exception;

public class PrioridadeNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public PrioridadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public PrioridadeNaoEncontradaException(Long idPrioridade) {
        this(String.format("Não existe uma prioridade cadastrada com o código %d", idPrioridade));
    }
}