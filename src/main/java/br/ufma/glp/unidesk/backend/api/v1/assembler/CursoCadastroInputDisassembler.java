package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CursoCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public Curso toDomainObject(CursoCadastroInput cursoCadastroInput){
        return modelMapper.map(cursoCadastroInput, Curso.class);
    }
}
