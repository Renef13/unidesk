package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.TicketMovimentacaoModel;
import br.ufma.glp.unidesk.backend.domain.model.TicketMovimentacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TicketMovimentacaoModelAssembler {
    private final UsuarioModelAssembler usuarioModelAssembler;

    public TicketMovimentacaoModel toModel(TicketMovimentacao mov) {
        if (mov == null) {
            return null;
        }
        TicketMovimentacaoModel model = new TicketMovimentacaoModel();
        model.setIdMovimentacao(mov.getIdMovimentacao());
        model.setTipo(mov.getTipo());
        model.setDataMovimentacao(mov.getDataMovimentacao());
        model.setUsuarioOrigem(usuarioModelAssembler.toModel(mov.getUsuarioOrigem()));
        model.setUsuarioDestino(usuarioModelAssembler.toModel(mov.getUsuarioDestino()));
        return model;
    }

    public List<TicketMovimentacaoModel> toCollectionModel(List<TicketMovimentacao> movs) {
        if (movs == null) {
            return List.of();
        }
        return movs.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
