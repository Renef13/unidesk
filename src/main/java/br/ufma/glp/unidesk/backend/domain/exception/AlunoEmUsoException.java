package br.ufma.glp.unidesk.backend.domain.exception;

public class AlunoEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public AlunoEmUsoException(String mensagem) {
        super(mensagem);
    }

    public AlunoEmUsoException(Long alunoId) {
        this(String.format("Aluno de código %d não pode ser removido, pois está em uso", alunoId));
    }
}