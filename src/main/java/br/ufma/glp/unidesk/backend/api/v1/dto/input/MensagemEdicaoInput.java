package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MensagemEdicaoInput {


    private Long idMensagem;

    private String conteudo;

    private Long idTicket;

    private Long idUsuario;
}