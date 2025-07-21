package br.ufma.glp.unidesk.backend.domain.exception;

public class FuncionarioCoordenacaoEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public FuncionarioCoordenacaoEmUsoException(String mensagem) {
        super(mensagem);
    }

    public FuncionarioCoordenacaoEmUsoException(Long idFuncionario) {
        this(String.format("Funcionário de coordenação de código %d não pode ser removido, pois está em uso", idFuncionario));
    }
}