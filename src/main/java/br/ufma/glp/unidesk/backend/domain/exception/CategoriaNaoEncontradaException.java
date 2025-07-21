package br.ufma.glp.unidesk.backend.domain.exception;

public class CategoriaNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public CategoriaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public CategoriaNaoEncontradaException(Long idCategoria) {
        this(String.format("Não existe uma categoria com código %d", idCategoria));
    }
}