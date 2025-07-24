package br.ufma.glp.unidesk.backend.domain.exception;

public class CursoNomeEmUsoException extends EntidadeEmUsoException {

    private static final long serialVersionUID = 1L;

    public CursoNomeEmUsoException(String nome) {
        super(String.format("Já existe um curso com o nome: %s", nome));
    }
}