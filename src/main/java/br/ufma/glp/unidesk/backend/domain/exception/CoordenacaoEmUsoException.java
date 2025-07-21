package br.ufma.glp.unidesk.backend.domain.exception;

public class CoordenacaoEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public CoordenacaoEmUsoException(String mensagem) {
        super(mensagem);
    }

    public CoordenacaoEmUsoException(Long idCoordenacao) {
        this(String.format("Coordenação de código %d não pode ser removida, pois está em uso", idCoordenacao));
    }
}