package br.ufma.glp.unidesk.backend.domain.exception;

public class NomeCoordenacaoJaExistenteException extends NegocioException {

    private static final long serialVersionUID = 1L;

    public NomeCoordenacaoJaExistenteException(String nome) {
        super(String.format("Já existe uma coordenação cadastrada com o nome '%s'", nome));
    }
}