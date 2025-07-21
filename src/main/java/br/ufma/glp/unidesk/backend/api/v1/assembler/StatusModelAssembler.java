package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.controller.StatusController;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.StatusModel;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class StatusModelAssembler extends RepresentationModelAssemblerSupport<Status, StatusModel> {

    @Autowired
    private final ModelMapper modelMapper;

    public StatusModelAssembler(ModelMapper modelMapper) {
        super(StatusController.class, StatusModel.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public StatusModel toModel(Status status) {
        StatusModel statusModel = modelMapper.map(status, StatusModel.class);
        return statusModel;
    }

    @Override
    public CollectionModel<StatusModel> toCollectionModel(Iterable<? extends Status> entities) {
        return super.toCollectionModel(entities);
    }
}