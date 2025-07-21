package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.PrioridadeModel;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrioridadeModelAssembler {

    private final ModelMapper modelMapper;

    public PrioridadeModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PrioridadeModel toModel(Prioridade prioridade) {
        return modelMapper.map(prioridade, PrioridadeModel.class);
    }

    public  List<PrioridadeModel> toCollectionModel(List<Prioridade> prioridades) {
        return prioridades.stream()
                .map(this::toModel)
                .toList();
    }
}
