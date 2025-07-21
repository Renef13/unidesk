package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.StatusCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatusCadastroInputDisassembler {

    private final ModelMapper modelMapper;

    public Status toDomainObject(StatusCadastroInput statusCadastroInput) {
        return modelMapper.map(statusCadastroInput, Status.class);
    }

    public void copyToDomainObject(StatusCadastroInput statusCadastroInput, Status status) {
        modelMapper.map(statusCadastroInput, status);
    }
}