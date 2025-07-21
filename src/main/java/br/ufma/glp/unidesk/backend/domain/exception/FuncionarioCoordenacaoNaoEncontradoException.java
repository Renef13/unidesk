package br.ufma.glp.unidesk.backend.domain.exception;

public class FuncionarioCoordenacaoNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public FuncionarioCoordenacaoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public FuncionarioCoordenacaoNaoEncontradoException(Long idFuncionario) {
        this(String.format("Não existe um funcionário de coordenação cadastrado com o código %d", idFuncionario));
    }
    
    public FuncionarioCoordenacaoNaoEncontradoException(String matricula, String tipo) {
        this(String.format("Não existe um funcionário de coordenação com a matrícula %s", matricula));
    }
}