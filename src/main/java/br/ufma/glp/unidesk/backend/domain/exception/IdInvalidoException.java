package br.ufma.glp.unidesk.backend.domain.exception;

public class IdInvalidoException extends EntidadeNaoEncontradaException  {

	private static final long serialVersionUID = 1L;

	public IdInvalidoException(String mensagem) {
		super("Digite um id válido para "+ mensagem);
	}
}