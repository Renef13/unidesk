package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenadorCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoordenadorCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public Coordenador toDomainObject(CoordenadorCadastroInput coordenadorCadastroInput) {
        return modelMapper.map(coordenadorCadastroInput, Coordenador.class);
    }
}
