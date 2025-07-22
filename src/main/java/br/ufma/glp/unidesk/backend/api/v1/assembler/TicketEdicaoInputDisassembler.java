package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.TicketEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public Ticket toDomainObject(TicketEdicaoInput ticketEdicaoInput) {
        Ticket ticket = modelMapper.map(ticketEdicaoInput, Ticket.class);

        if (ticketEdicaoInput.getIdCoordenacao() != null) {
            Coordenacao coordenacao = new Coordenacao();
            coordenacao.setIdCoordenacao(ticketEdicaoInput.getIdCoordenacao());
            ticket.setCoordenacao(coordenacao);
        }
        if (ticketEdicaoInput.getIdFuncionario() != null) {
            FuncionarioCoordenacao funcionario = new FuncionarioCoordenacao();
            funcionario.setIdUsuario(ticketEdicaoInput.getIdFuncionario());
            ticket.setFuncionario(funcionario);
        }
        if (ticketEdicaoInput.getIdAluno() != null) {
            Aluno aluno = new Aluno();
            aluno.setIdUsuario(ticketEdicaoInput.getIdAluno());
            ticket.setAluno(aluno);
        }
        if (ticketEdicaoInput.getIdStatus() != null) {
            Status status = new Status();
            status.setIdStatus(ticketEdicaoInput.getIdStatus());
            ticket.setStatus(status);
        }
        if (ticketEdicaoInput.getIdPrioridade() != null) {
            Prioridade prioridade = new Prioridade();
            prioridade.setIdPrioridade(ticketEdicaoInput.getIdPrioridade());
            ticket.setPrioridade(prioridade);
        }
        if (ticketEdicaoInput.getIdCategoria() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(ticketEdicaoInput.getIdCategoria());
            ticket.setCategoria(categoria);
        }
        return ticket;
    }


}
