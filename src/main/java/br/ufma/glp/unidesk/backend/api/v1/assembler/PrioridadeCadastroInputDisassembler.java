package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.PrioridadeCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PrioridadeCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public PrioridadeCadastroInputDisassembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public Prioridade toDomainObject(PrioridadeCadastroInput prioridadeCadastroInput) {
        return modelMapper.map(prioridadeCadastroInput, Prioridade.class);
    }

    public void copyToDomainObject(PrioridadeCadastroInput prioridadeCadastroInput, Prioridade prioridade) {
        modelMapper.map(prioridadeCadastroInput, prioridade);
    }
}
