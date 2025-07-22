package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.UsuarioCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public Usuario toDomainObject(UsuarioCadastroInput usuarioCadastroInput) {
        return modelMapper.map(usuarioCadastroInput, Usuario.class);
    }
}
