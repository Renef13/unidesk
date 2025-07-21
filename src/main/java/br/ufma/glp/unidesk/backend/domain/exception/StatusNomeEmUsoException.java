package br.ufma.glp.unidesk.backend.domain.exception;

public class StatusNomeEmUsoException extends NegocioException {

    private static final long serialVersionUID = 1L;


    public StatusNomeEmUsoException(String mensagem) {
        super(mensagem);
    }

    public static StatusNomeEmUsoException comNome(String nome) {
        return new StatusNomeEmUsoException(
                String.format("Já existe um status cadastrado com o nome '%s'", nome)
        );
    }
}