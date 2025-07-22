package br.ufma.glp.unidesk.backend.domain.exception;

public class AlunoNaoPodeSerExcluidoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public AlunoNaoPodeSerExcluidoException(String mensagem) {
        super(mensagem);
    }

    public AlunoNaoPodeSerExcluidoException(Long alunoId) {
        this(String.format("Aluno de código %d não pode ser excluído pois está vinculado a tickets.", alunoId));
    }
}