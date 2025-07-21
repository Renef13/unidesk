package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.AlunoCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlunoCadastroInputDisassembler {

    private final ModelMapper modelMapper;

    public Aluno toDomainObject(AlunoCadastroInput input) {
        Aluno aluno = modelMapper.map(input, Aluno.class);

        if (input.getIdCurso() != null) {
            Curso curso = new Curso();
            curso.setIdCurso(input.getIdCurso());
            aluno.setCurso(curso);
        }

        return aluno;
    }
}
