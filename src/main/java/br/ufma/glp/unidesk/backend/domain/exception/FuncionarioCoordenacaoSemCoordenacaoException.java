package br.ufma.glp.unidesk.backend.domain.exception;

public class FuncionarioCoordenacaoSemCoordenacaoException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public FuncionarioCoordenacaoSemCoordenacaoException(String mensagem) {
        super(mensagem);
    }

    public FuncionarioCoordenacaoSemCoordenacaoException() {
        this("O funcionário deve estar vinculado a uma coordenação válida");
    }
}