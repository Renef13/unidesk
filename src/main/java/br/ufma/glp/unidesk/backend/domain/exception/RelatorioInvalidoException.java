package br.ufma.glp.unidesk.backend.domain.exception;

public class RelatorioInvalidoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public RelatorioInvalidoException(String mensagem) {
        super(mensagem);
    }
}