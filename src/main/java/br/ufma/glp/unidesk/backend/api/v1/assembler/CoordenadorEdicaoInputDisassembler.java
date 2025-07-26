package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenadorEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoordenadorEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public void copyToDomainObject(CoordenadorEdicaoInput coordenadorEdicaoInput, Coordenador coordenador) {

        modelMapper.map(coordenadorEdicaoInput, coordenador);
        if (coordenadorEdicaoInput.getIdCoordenacao() != null) {
            Coordenacao coordenacao = new Coordenacao();
            coordenacao.setIdCoordenacao(coordenadorEdicaoInput.getIdCoordenacao());
            coordenador.setCoordenacao(coordenacao);
        }

    }

}
