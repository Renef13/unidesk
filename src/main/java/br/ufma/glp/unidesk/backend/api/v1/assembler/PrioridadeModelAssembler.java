package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.PrioridadeModel;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PrioridadeModelAssembler {

    private final ModelMapper modelMapper;

    public PrioridadeModel toModel(Prioridade prioridade) {
        if (prioridade == null) {
            return null;
        }
        return modelMapper.map(prioridade, PrioridadeModel.class);
    }

    public  List<PrioridadeModel> toCollectionModel(List<Prioridade> prioridades) {
        if (prioridades == null) {
            return List.of();
        }
        return prioridades.stream()
                .map(this::toModel)
                .toList();
    }
}
