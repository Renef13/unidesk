package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.CursoModel;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CursoModelAssembler {
    private final ModelMapper modelMapper;

    public CursoModel toModel(Curso curso) {
        if (curso == null) {
            return null;
        }
        return modelMapper.map(curso, CursoModel.class);
    }

    public List<CursoModel> toCollectionModel(List<Curso> cursos) {
        if (cursos == null) {
            return List.of();
        }
        return cursos.stream()
                .map(this::toModel)
                .toList();
    }

}
