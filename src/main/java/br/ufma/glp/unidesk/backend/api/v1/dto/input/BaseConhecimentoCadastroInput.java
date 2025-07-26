package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseConhecimentoCadastroInput {

    private String titulo;

    private String conteudo;

    @NotNull(message = "A categoria do artigo é obrigatória")
    private Long idCategoria;
}