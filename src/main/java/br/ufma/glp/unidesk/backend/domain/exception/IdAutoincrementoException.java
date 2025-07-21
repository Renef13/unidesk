package br.ufma.glp.unidesk.backend.domain.exception;

public abstract class IdAutoincrementoException extends NegocioException{

	private static final long serialVersionUID = 1L;
	
	private static final String mensagem = "O id não pode ser definido pela camada de aplicação";

	public IdAutoincrementoException(String mensagem) {
		super(mensagem == null || mensagem.isEmpty() ? IdAutoincrementoException.mensagem : mensagem);
	}
	
}
