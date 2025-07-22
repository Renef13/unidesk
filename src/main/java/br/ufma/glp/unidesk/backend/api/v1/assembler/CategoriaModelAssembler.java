package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.CategoriaModel;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoriaModelAssembler {

    private final ModelMapper modelMapper;

    public CategoriaModel toModel(Categoria categoria) {
        if( categoria == null) {
            return null;
        }
        return modelMapper.map(categoria, CategoriaModel.class);
    }

    public List<CategoriaModel> toCollectionModel(List<Categoria> categorias) {
        if (categorias == null || categorias.isEmpty()) {
            return List.of();
        }
        return categorias.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

}
