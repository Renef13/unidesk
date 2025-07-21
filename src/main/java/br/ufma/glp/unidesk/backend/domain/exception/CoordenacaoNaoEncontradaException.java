package br.ufma.glp.unidesk.backend.domain.exception;

public class CoordenacaoNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public CoordenacaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public CoordenacaoNaoEncontradaException(Long idCoordenacao) {
        this(String.format("Não existe uma coordenação com código %d", idCoordenacao));
    }
}