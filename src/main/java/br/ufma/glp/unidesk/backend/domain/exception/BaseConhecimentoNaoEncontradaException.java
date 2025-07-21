package br.ufma.glp.unidesk.backend.domain.exception;

public class BaseConhecimentoNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public BaseConhecimentoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
    
    public BaseConhecimentoNaoEncontradaException(Long idArtigo) {
        this(String.format("Não existe um artigo de base de conhecimento com código %d", idArtigo));
    }
}