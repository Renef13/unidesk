package br.ufma.glp.unidesk.backend.domain.exception;

public class MensagemNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public MensagemNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public MensagemNaoEncontradaException(Long idMensagem) {
        this(String.format("Não existe uma mensagem cadastrada com o código %d", idMensagem));
    }
}