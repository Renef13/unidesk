package br.ufma.glp.unidesk.backend.domain.exception;

public class CoordenacaoSemCursoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public CoordenacaoSemCursoException() {
        super("A coordenação precisa estar vinculada a um curso");
    }
}