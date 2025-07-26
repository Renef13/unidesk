package br.ufma.glp.unidesk.backend.api.v1.assembler;


import br.ufma.glp.unidesk.backend.api.v1.dto.input.FuncionarioCoordenacaoEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FuncionarioCoordenacaoEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public void copyToDomainObject(FuncionarioCoordenacaoEdicaoInput funcionarioCoordenacaoEdicaoInput, FuncionarioCoordenacao funcionarioCoordenacao) {
        modelMapper.map(funcionarioCoordenacaoEdicaoInput, funcionarioCoordenacao);
        if (funcionarioCoordenacaoEdicaoInput.getIdCoordenacao() != null) {
            Coordenacao coordenacao = new Coordenacao();
            coordenacao.setIdCoordenacao(funcionarioCoordenacaoEdicaoInput.getIdCoordenacao());
            funcionarioCoordenacao.setCoordenacao(coordenacao);
        }
    }
}
