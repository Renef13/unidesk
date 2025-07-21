package br.ufma.glp.unidesk.backend.domain.exception;

public class UsuarioJaCadastradoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public UsuarioJaCadastradoException(String mensagem) {
        super(mensagem);
    }
    
    public UsuarioJaCadastradoException(String campo, String valor) {
        this(String.format("Já existe um usuário cadastrado com %s: %s", campo, valor));
    }
}