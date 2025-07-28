package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.UsuarioModel;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioModelAssembler {
    private final ModelMapper modelMapper;

    public UsuarioModel toModel(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return modelMapper.map(usuario, UsuarioModel.class);
    }

    public List<UsuarioModel> toCollectionModel(List<Usuario> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            return List.of();
        }
        return usuarios.stream()
                .map(this::toModel)
                .toList();
    }
}
