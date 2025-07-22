package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.UsuarioEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioEdicaoInputDisassembler {
    private final ModelMapper modelMapper;

    public Usuario toDomainObject(UsuarioEdicaoInput usuarioEdicaoInput) {
        return modelMapper.map(usuarioEdicaoInput, Usuario.class);
    }
}
