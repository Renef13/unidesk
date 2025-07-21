package br.ufma.glp.unidesk.backend.domain.exception;

public class CategoriaEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public CategoriaEmUsoException(String mensagem) {
        super(mensagem);
    }

    public CategoriaEmUsoException(Long idCategoria) {
        this(String.format("Categoria de código %d não pode ser removida, pois está em uso", idCategoria));
    }
}