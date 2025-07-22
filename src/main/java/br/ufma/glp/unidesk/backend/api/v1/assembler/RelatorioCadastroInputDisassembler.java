package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.RelatorioCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Relatorio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RelatorioCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public Relatorio toDomainObject(RelatorioCadastroInput relatorioCadastroInput) {
        return modelMapper.map(relatorioCadastroInput, Relatorio.class);
    }
}
