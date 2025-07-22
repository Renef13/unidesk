package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.UsuarioModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioModelAssembler {
    private final ModelMapper modelMapper;

    public UsuarioModel toModel(UsuarioModel usuarioModel) {
        if (usuarioModel == null) {
            return null;
        }
        return modelMapper.map(usuarioModel, UsuarioModel.class);
    }

    public List<UsuarioModel> toCollectionModel(List<UsuarioModel> usuarioModels) {
        if (usuarioModels == null || usuarioModels.isEmpty()) {
            return List.of();
        }
        return usuarioModels.stream()
                .map(this::toModel)
                .toList();
    }
}
