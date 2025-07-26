package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenadorCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoordenadorCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public Coordenador toDomainObject(CoordenadorCadastroInput coordenadorCadastroInput) {
        Coordenador coordenador = modelMapper.map(coordenadorCadastroInput, Coordenador.class);

        if (coordenadorCadastroInput.getIdCoordenacao() != null) {
            Coordenacao coordenacao = new Coordenacao();
            coordenacao.setIdCoordenacao(coordenadorCadastroInput.getIdCoordenacao());
            coordenador.setCoordenacao(coordenacao);
        }
        return coordenador;
    }
}
