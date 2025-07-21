package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensagemModel {

    private Long idMensagem;
    
    private String conteudo;
    
    private UsuarioModel usuario;
    
    private TicketModel ticket;
}