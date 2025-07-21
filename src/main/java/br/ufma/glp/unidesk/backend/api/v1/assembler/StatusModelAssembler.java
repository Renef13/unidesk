package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.StatusModel;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StatusModelAssembler {

    private final ModelMapper modelMapper;

    public StatusModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public StatusModel toModel(Status status) {
        return modelMapper.map(status, StatusModel.class);
    }

    public List<StatusModel> toCollectionModel(List<Status> statuses) {
        return statuses.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
