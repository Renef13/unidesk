package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.MensagemModel;
import br.ufma.glp.unidesk.backend.domain.model.Mensagem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MensagemModelAssembler {
    private final ModelMapper modelMapper;

    public MensagemModel toModel(Mensagem mensagem) {
        if (mensagem == null) {
            return null;
        }
        return modelMapper.map(mensagem, MensagemModel.class);
    }

    public List<MensagemModel> toCollectionModel(List<Mensagem> mensagens) {
        if (mensagens == null) {
            return List.of();
        }
        return mensagens.stream()
                .map(this::toModel)
                .toList();
    }
}
