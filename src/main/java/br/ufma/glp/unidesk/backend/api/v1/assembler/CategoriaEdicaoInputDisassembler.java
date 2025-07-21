package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.CategoriaEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CategoriaEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public Categoria toDomainObject(CategoriaEdicaoInput categoriaEdicaoInput) {
        return modelMapper.map(categoriaEdicaoInput, Categoria.class);
    }

    public void copyToDomainObject(CategoriaEdicaoInput categoriaEdicaoInput, Categoria categoria) {
        modelMapper.map(categoriaEdicaoInput, categoria);
    }

}
