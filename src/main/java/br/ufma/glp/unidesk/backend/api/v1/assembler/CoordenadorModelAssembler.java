package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenadorModel;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CoordenadorModelAssembler {
    private final ModelMapper modelMapper;

    public CoordenadorModel toModel(Coordenador coordenador) {
        if (coordenador == null) {
            return null;
        }
        return modelMapper.map(coordenador, CoordenadorModel.class);
    }

    public List<CoordenadorModel> toCollectionModel(List<Coordenador> coordenadores) {
        if (coordenadores == null) {
            return List.of();
        }
        return coordenadores.stream()
                .map(this::toModel)
                .toList();
    }

}
