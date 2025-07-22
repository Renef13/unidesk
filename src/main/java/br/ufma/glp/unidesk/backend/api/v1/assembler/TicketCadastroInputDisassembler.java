package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.TicketCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public Ticket toDomainObject(TicketCadastroInput ticketCadastroInput) {
        Ticket ticket = modelMapper.map(ticketCadastroInput, Ticket.class);

        if (ticketCadastroInput.getIdCoordenacao() != null) {
            Coordenacao coordenacao = new Coordenacao();
            coordenacao.setIdCoordenacao(ticketCadastroInput.getIdCoordenacao());
            ticket.setCoordenacao(coordenacao);
        }
        if (ticketCadastroInput.getIdFuncionario() != null) {
            FuncionarioCoordenacao funcionario = new FuncionarioCoordenacao();
            funcionario.setIdUsuario(ticketCadastroInput.getIdFuncionario());
            ticket.setFuncionario(funcionario);
        }
        if (ticketCadastroInput.getIdAluno() != null) {
            Aluno aluno = new Aluno();
            aluno.setIdUsuario(ticketCadastroInput.getIdAluno());
            ticket.setAluno(aluno);
        }
        if (ticketCadastroInput.getIdStatus() != null) {
            Status status = new Status();
            status.setIdStatus(ticketCadastroInput.getIdStatus());
            ticket.setStatus(status);
        }
        if (ticketCadastroInput.getIdPrioridade() != null) {
            Prioridade prioridade = new Prioridade();
            prioridade.setIdPrioridade(ticketCadastroInput.getIdPrioridade());
            ticket.setPrioridade(prioridade);
        }
        if (ticketCadastroInput.getIdCategoria() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(ticketCadastroInput.getIdCategoria());
            ticket.setCategoria(categoria);
        }
        return ticket;
    }

}
