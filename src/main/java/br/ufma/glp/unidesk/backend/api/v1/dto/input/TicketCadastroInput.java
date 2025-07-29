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
public class TicketCadastroInput {

    @NotBlank(message = "Título não pode ser vazio")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "Descrição não pode ser vazia")
    private String descricao;

    @NotNull(message = "ID da coordenação é obrigatório")
    private Long idCoordenacao;

    private Long idFuncionario;

    @NotNull(message = "ID do aluno é obrigatório")
    private Long idAluno;

    @NotNull(message = "ID do status é obrigatório")
    private Long idStatus;

    @NotNull(message = "ID da prioridade é obrigatória")
    private Long idPrioridade;

    @NotNull(message = "ID da categoria é obrigatória")
    private Long idCategoria;
}