package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CategoriaCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CategoriaCadastroInputDisassembler {

    private final ModelMapper modelMapper;

    public Categoria toDoaminObject(CategoriaCadastroInput categoriaCadastroInput) {
        return modelMapper.map(categoriaCadastroInput, Categoria.class);
    }

    public void copyToDomainObject(CategoriaCadastroInput categoriaCadastroInput, Categoria categoria) {
        modelMapper.map(categoriaCadastroInput, categoria);
    }


}
