package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idTicket")
public class TicketModel {

    private Long idTicket;
    
    private String titulo;
    
    private String descricao;
    
    private Instant dataCriacao;
    
    private Instant dataFechamento;
    
    private Instant dataAtualizacao;
    
    private CoordenacaoModel coordenacao;
    
    private FuncionarioCoordenacaoModel funcionario;
    
    private AlunoModel aluno;
    
    private StatusModel status;
    
    private PrioridadeModel prioridade;
    
    private CategoriaModel categoria;
}