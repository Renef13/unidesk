package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenacaoEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoordenacaoEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public Coordenacao toDomainObject(CoordenacaoEdicaoInput coordenacaoEdicaoInput) {
        Coordenacao coordenacao = modelMapper.map(coordenacaoEdicaoInput, Coordenacao.class);

        if (coordenacaoEdicaoInput.getIdCurso() != null) {
            Curso curso = new Curso();
            curso.setIdCurso(coordenacaoEdicaoInput.getIdCurso());
            coordenacao.setCurso(curso);
        }

        return coordenacao;

    }
}
