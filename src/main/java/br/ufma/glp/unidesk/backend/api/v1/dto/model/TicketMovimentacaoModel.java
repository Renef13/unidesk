package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.UsuarioModel;
import br.ufma.glp.unidesk.backend.domain.model.TipoMovimentacao;
import java.time.Instant;

public class TicketMovimentacaoModel {
    private Long idMovimentacao;
    private UsuarioModel usuarioOrigem;
    private UsuarioModel usuarioDestino;
    private TipoMovimentacao tipo;
    private Instant dataMovimentacao;

    public Long getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(Long idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public UsuarioModel getUsuarioOrigem() {
        return usuarioOrigem;
    }

    public void setUsuarioOrigem(UsuarioModel usuarioOrigem) {
        this.usuarioOrigem = usuarioOrigem;
    }

    public UsuarioModel getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(UsuarioModel usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public Instant getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Instant dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }
}
