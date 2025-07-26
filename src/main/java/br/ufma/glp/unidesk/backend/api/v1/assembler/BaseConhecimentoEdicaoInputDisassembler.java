package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseConhecimentoEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public void copyToDomainObject(BaseConhecimentoEdicaoInput baseConhecimentoInput, BaseConhecimento baseConhecimento) {
        modelMapper.map(baseConhecimentoInput, baseConhecimento);

        if (baseConhecimentoInput.getIdCategoria() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(baseConhecimentoInput.getIdCategoria());
            baseConhecimento.setCategoria(categoria);
        }
    }
}
