package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.PrioridadeEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrioridadeEdicaoInputDisassembler {
    private final ModelMapper modelMapper;

    public void copyToDomainObject(PrioridadeEdicaoInput prioridadeEdicaoInput, Prioridade prioridade) {
        modelMapper.map(prioridadeEdicaoInput, prioridade);
    }
}
