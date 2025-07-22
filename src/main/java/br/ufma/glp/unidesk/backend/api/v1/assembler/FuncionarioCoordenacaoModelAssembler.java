package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.FuncionarioCoordenacaoModel;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FuncionarioCoordenacaoModelAssembler {
    private final ModelMapper modelMapper;

    public FuncionarioCoordenacaoModel toModel(FuncionarioCoordenacao funcionarioCoordenacao) {
        if (funcionarioCoordenacao == null) {
            return null;
        }
        return modelMapper.map(funcionarioCoordenacao, FuncionarioCoordenacaoModel.class);
    }

    public List<FuncionarioCoordenacaoModel> toCollectionModel(List<FuncionarioCoordenacao> funcionariosCoordenacao) {
        if (funcionariosCoordenacao == null) {
            return List.of();
        }
        return funcionariosCoordenacao.stream()
                .map(this::toModel)
                .toList();
    }

}
