package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseConhecimentoCadastroInputDisassembler {

    private final ModelMapper modelMapper;

    public BaseConhecimento toDomainObject(BaseConhecimentoCadastroInput input) {
        BaseConhecimento baseConhecimento = modelMapper.map(input, BaseConhecimento.class);

        if (input.getIdCategoria() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(input.getIdCategoria());
            baseConhecimento.setCategoria(categoria);
        }

        return baseConhecimento;
    }
}
