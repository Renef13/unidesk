package br.ufma.glp.unidesk.backend.domain.exception;

public class AlunoEmailEmUsoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public AlunoEmailEmUsoException(String email) {
        super(String.format("Já existe um aluno cadastrado com o email: %s", email));
    }
}