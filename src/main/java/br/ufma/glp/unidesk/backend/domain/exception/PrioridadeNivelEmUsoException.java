package br.ufma.glp.unidesk.backend.domain.exception;

public class PrioridadeNivelEmUsoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public PrioridadeNivelEmUsoException(String mensagem) {
        super(mensagem);
    }

    public PrioridadeNivelEmUsoException(String nivel) {
        this(String.format("Já existe uma prioridade cadastrada com o nível '%s'", nivel));
    }
}