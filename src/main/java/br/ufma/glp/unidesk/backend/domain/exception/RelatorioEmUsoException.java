package br.ufma.glp.unidesk.backend.domain.exception;

public class RelatorioEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public RelatorioEmUsoException(String mensagem) {
        super(mensagem);
    }

    public RelatorioEmUsoException(Long idRelatorio) {
        this(String.format("Relatório de código %d não pode ser removido, pois está em uso", idRelatorio));
    }
}