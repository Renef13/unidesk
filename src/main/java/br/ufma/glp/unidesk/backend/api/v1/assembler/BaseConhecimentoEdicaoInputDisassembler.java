package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseConhecimentoEdicaoInputDisassembler {
    private final ModelMapper modelMapper;

    public BaseConhecimento toDomainObject(BaseConhecimentoCadastroInput baseConhecimentoCadastroInput) {
        BaseConhecimento baseConhecimento = modelMapper.map(baseConhecimentoCadastroInput, BaseConhecimento.class);

        if (baseConhecimentoCadastroInput.getIdCategoria() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(baseConhecimentoCadastroInput.getIdCategoria());
            baseConhecimento.setCategoria(categoria);
        }

        return baseConhecimento;
    }
}
