package br.ufma.glp.unidesk.backend.domain.exception;

public class TituloBaseConhecimentoJaExistenteException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public TituloBaseConhecimentoJaExistenteException(String titulo) {
        super(String.format("Já existe um artigo cadastrado com o título '%s'", titulo));
    }
}