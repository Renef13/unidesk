package br.ufma.glp.unidesk.backend.domain.exception;

public class UsuarioEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public UsuarioEmUsoException(String mensagem) {
        super(mensagem);
    }

    public UsuarioEmUsoException(Long idUsuario) {
        this(String.format("Usuário de código %d não pode ser removido, pois está em uso", idUsuario));
    }
}