package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.StatusEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatusEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public void copyToDomainObject(StatusEdicaoInput statusEdicaoInput, Status status) {
        modelMapper.map(statusEdicaoInput, status);
    }
}