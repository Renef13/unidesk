package br.ufma.glp.unidesk.backend.domain.exception;

public class BaseConhecimentoEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public BaseConhecimentoEmUsoException(String mensagem) {
        super(mensagem);
    }
    
    public BaseConhecimentoEmUsoException(Long idArtigo) {
        this(String.format("Artigo de código %d não pode ser removido, pois está em uso", idArtigo));
    }
}