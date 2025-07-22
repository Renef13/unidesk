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

    public Curso toDomainObject(CursoEdicaoInput cursoEdicaoInput) {
        return modelMapper.map(cursoEdicaoInput, Curso.class);
    }
}
