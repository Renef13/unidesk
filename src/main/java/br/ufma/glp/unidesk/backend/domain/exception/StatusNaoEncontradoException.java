package br.ufma.glp.unidesk.backend.domain.exception;

public class StatusNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public StatusNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public StatusNaoEncontradoException(Long idStatus) {
        this(String.format("Não existe um status cadastrado com o código %d", idStatus));
    }
}