package br.ufma.glp.unidesk.backend.domain.exception;

public class CoordenadorNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public CoordenadorNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public CoordenadorNaoEncontradoException(Long idCoordenador) {
        this(String.format("Não existe um coordenador com código %d", idCoordenador));
    }
    
    public CoordenadorNaoEncontradoException(String matricula, boolean isMatricula) {
        this(String.format("Não existe um coordenador com matrícula %s", matricula));
    }
}