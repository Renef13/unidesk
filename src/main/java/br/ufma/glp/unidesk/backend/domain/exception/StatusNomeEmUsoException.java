package br.ufma.glp.unidesk.backend.domain.exception;

public class StatusNomeEmUsoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public StatusNomeEmUsoException(String mensagem) {
        super(mensagem);
    }

    public StatusNomeEmUsoException(String nome) {
        this(String.format("Já existe um status cadastrado com o nome '%s'", nome));
    }
}