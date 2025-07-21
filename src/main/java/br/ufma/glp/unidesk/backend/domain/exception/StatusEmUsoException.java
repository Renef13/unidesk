package br.ufma.glp.unidesk.backend.domain.exception;

public class StatusEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public StatusEmUsoException(String mensagem) {
        super(mensagem);
    }

    public StatusEmUsoException(Long idStatus) {
        this(String.format("Status de código %d não pode ser removido, pois está em uso", idStatus));
    }
}