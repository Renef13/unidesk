package br.ufma.glp.unidesk.backend.api.v1.assembler;


import br.ufma.glp.unidesk.backend.api.v1.dto.input.FuncionarioCoordenacaoCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FuncionarioCoordenacaoCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public FuncionarioCoordenacao toDomainObject(FuncionarioCoordenacaoCadastroInput funcionarioCoordenacaoCadastroInput) {
        return modelMapper.map(funcionarioCoordenacaoCadastroInput, FuncionarioCoordenacao.class);
    }
}
