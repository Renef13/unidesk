package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.CategoriaModel;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoriaModelAssembler {

    private final ModelMapper modelMapper;

    public CategoriaModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoriaModel toModel(Categoria categoria) {
        return modelMapper.map(categoria, CategoriaModel.class);
    }

    public List<CategoriaModel> toCollectionModel(List<Categoria> categorias) {
        return categorias.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

}
