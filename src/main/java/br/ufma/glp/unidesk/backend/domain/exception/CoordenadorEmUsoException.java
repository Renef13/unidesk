package br.ufma.glp.unidesk.backend.domain.exception;

public class CoordenadorEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public CoordenadorEmUsoException(String mensagem) {
        super(mensagem);
    }

    public CoordenadorEmUsoException(Long idCoordenador) {
        this(String.format("Coordenador de código %d não pode ser removido, pois está em uso", idCoordenador));
    }
}