package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.PrioridadeCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrioridadeCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public Prioridade toDomainObject(PrioridadeCadastroInput prioridadeCadastroInput) {
        return modelMapper.map(prioridadeCadastroInput, Prioridade.class);
    }

}
