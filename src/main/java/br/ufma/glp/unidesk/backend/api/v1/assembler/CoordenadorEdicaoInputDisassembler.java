package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenadorEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoordenadorEdicaoInputDisassembler {
    private final ModelMapper modelMapper;

    public Coordenador toDomainObject(CoordenadorEdicaoInput coordenadorEdicaoInput) {
        return modelMapper.map(coordenadorEdicaoInput, Coordenador.class);
    }

}
