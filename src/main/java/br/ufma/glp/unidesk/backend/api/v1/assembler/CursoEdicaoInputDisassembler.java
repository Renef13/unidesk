package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CursoEdicaoInputDisassembler {
    private final ModelMapper modelMapper;

    public void copyToDomainObject(CursoEdicaoInput cursoEdicaoInput, Curso curso) {
        modelMapper.map(cursoEdicaoInput, curso);
    }
}
