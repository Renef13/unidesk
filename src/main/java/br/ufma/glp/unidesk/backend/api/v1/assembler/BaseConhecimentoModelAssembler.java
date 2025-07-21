package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.BaseConhecimentoModel;
import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BaseConhecimentoModelAssembler {
    private final ModelMapper modelMapper;

    public BaseConhecimentoModel toModel(BaseConhecimento baseConhecimento) {
        if( baseConhecimento == null) {
            return null;
        }
        return modelMapper.map(baseConhecimento, BaseConhecimentoModel.class);
    }

    public List<BaseConhecimentoModel> toCollectionModel(List<BaseConhecimento> basesConhecimento) {
        if (basesConhecimento == null || basesConhecimento.isEmpty()) {
            return List.of();
        }
        return basesConhecimento.stream()
                .map(this::toModel)
                .toList();
    }
}
