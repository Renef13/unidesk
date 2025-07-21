package br.ufma.glp.unidesk.backend.domain.exception;

public class CursoNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;
    
    public CursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    
    public CursoNaoEncontradoException(Long cursoId) {
        this(String.format("Não existe um cadastro de curso com código %d", cursoId));
    }
}