package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.RelatorioModel;
import br.ufma.glp.unidesk.backend.domain.model.Relatorio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RelatorioModelAssembler {

    private final ModelMapper modelMapper;

    public RelatorioModel toModel(Relatorio relatorio) {
        if (relatorio == null) {
            return null;
        }
        return modelMapper.map(relatorio, RelatorioModel.class);
    }

    public List<RelatorioModel> toCollectionModel(List<Relatorio> relatorios) {
        if (relatorios == null) {
            return List.of();
        }
        return relatorios.stream()
                .map(this::toModel)
                .toList();
    }
}
