package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.AlunoModel;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AlunoModelAssembler {

    private final ModelMapper modelMapper;

    public AlunoModel toModel(Aluno aluno) {
        if (aluno == null) {
            return null;
        }
        return modelMapper.map(aluno, AlunoModel.class);
    }

    public List<AlunoModel> toCollectionModel(List<Aluno> alunos) {
        if (alunos == null) {
            return List.of();
        }
        return alunos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}