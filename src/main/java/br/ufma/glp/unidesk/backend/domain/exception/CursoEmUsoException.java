package br.ufma.glp.unidesk.backend.domain.exception;

public class CursoEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;
    
    public CursoEmUsoException(String mensagem) {
        super(mensagem);
    }
    
    public CursoEmUsoException(Long cursoId) {
        this(String.format("Curso de código %d não pode ser removido, pois está em uso", cursoId));
    }
}