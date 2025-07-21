package br.ufma.glp.unidesk.backend.domain.exception;

public class MatriculaJaExistenteException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public MatriculaJaExistenteException(String matricula) {
        super(String.format("Já existe um aluno cadastrado com a matrícula %s", matricula));
    }
}