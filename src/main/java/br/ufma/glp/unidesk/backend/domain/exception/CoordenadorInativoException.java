package br.ufma.glp.unidesk.backend.domain.exception;

public class CoordenadorInativoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public CoordenadorInativoException(String mensagem) {
        super(mensagem);
    }

    public CoordenadorInativoException(Long idCoordenador) {
        this(String.format("Coordenador de código %d está inativo", idCoordenador));
    }
    
    public CoordenadorInativoException(String matricula, boolean isMatricula) {
        this(String.format("Coordenador com matrícula %s está inativo", matricula));
    }
}