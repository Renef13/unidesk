package br.ufma.glp.unidesk.backend.api.v1.assembler;


import br.ufma.glp.unidesk.backend.api.v1.dto.input.FuncionarioCoordenacaoEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FuncionarioCoordenacaoEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public FuncionarioCoordenacao toDomainObject(FuncionarioCoordenacaoEdicaoInput funcionarioCoordenacaoEdicaoInput){
        return modelMapper.map(funcionarioCoordenacaoEdicaoInput, FuncionarioCoordenacao.class);
    }
}
