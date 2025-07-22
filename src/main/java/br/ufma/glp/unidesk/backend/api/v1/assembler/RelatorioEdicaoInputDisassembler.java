package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.RelatorioEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Relatorio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RelatorioEdicaoInputDisassembler {
    private final ModelMapper modelMapper;

    public Relatorio toDomainObject(RelatorioEdicaoInput relatorioEdicaoInput) {
        return modelMapper.map(relatorioEdicaoInput, Relatorio.class);
    }
}
