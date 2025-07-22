package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenacaoModel;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CoordenacaoModelAssembler {

    private final ModelMapper modelMapper;

    public CoordenacaoModel toModel(Coordenacao coordenacao) {
        if (coordenacao == null) {
            return null;
        }
        return modelMapper.map(coordenacao, CoordenacaoModel.class);
    }

    public List<CoordenacaoModel> toCollectionModel(List<Coordenacao> coordenacoes) {
        if (coordenacoes == null) {
            return List.of();
        }
        return coordenacoes.stream()
                .map(this::toModel)
                .toList();
    }
}
