package br.ufma.glp.unidesk.backend.api.v1.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketEdicaoInput {

    private String titulo;

    private String descricao;

    private String mensagem;

    private Long idCoordenacao;

    private Long idFuncionario;

    private Long idAluno;

    private Long idStatus;

    private Long idPrioridade;

    private Long idCategoria;
}