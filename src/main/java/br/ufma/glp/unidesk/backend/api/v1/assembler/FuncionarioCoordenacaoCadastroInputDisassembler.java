package br.ufma.glp.unidesk.backend.api.v1.assembler;


import br.ufma.glp.unidesk.backend.api.v1.dto.input.FuncionarioCoordenacaoCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FuncionarioCoordenacaoCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public FuncionarioCoordenacao toDomainObject(FuncionarioCoordenacaoCadastroInput funcionarioCoordenacaoCadastroInput) {
        FuncionarioCoordenacao funcionarioCoordenacao =  modelMapper.map(funcionarioCoordenacaoCadastroInput, FuncionarioCoordenacao.class);
        if (funcionarioCoordenacaoCadastroInput.getIdCoordenacao() != null) {
            Coordenacao coordenacao = new Coordenacao();
            coordenacao.setIdCoordenacao(funcionarioCoordenacaoCadastroInput.getIdCoordenacao());
            funcionarioCoordenacao.setCoordenacao(coordenacao);
        }
        return funcionarioCoordenacao;

    }
}
