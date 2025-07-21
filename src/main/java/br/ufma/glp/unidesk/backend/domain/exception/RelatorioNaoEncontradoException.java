package br.ufma.glp.unidesk.backend.domain.exception;

public class RelatorioNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public RelatorioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RelatorioNaoEncontradoException(Long idRelatorio) {
        this(String.format("Não existe um relatório cadastrado com o código %d", idRelatorio));
    }
}