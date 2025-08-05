package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.TicketEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    @Transactional
    public void copyToDomainObject(TicketEdicaoInput ticketEdicaoInput, Ticket ticketExistente) {
        modelMapper.typeMap(TicketEdicaoInput.class, Ticket.class)
                   .addMappings(mapper -> {
                       mapper.skip(Ticket::setCoordenacao);
                       mapper.skip(Ticket::setFuncionario);
                       mapper.skip(Ticket::setAluno);
                       mapper.skip(Ticket::setStatus);
                       mapper.skip(Ticket::setPrioridade);
                       mapper.skip(Ticket::setCategoria);
                   })
                   .map(ticketEdicaoInput, ticketExistente);

        if (ticketEdicaoInput.getIdCoordenacao() != null) {
            Coordenacao coord = new Coordenacao();
            coord.setIdCoordenacao(ticketEdicaoInput.getIdCoordenacao());
            ticketExistente.setCoordenacao(coord);
        }

        if (ticketEdicaoInput.getIdFuncionario() != null) {
            FuncionarioCoordenacao func = new FuncionarioCoordenacao();
            func.setIdUsuario(ticketEdicaoInput.getIdFuncionario());
            ticketExistente.setFuncionario(func);
        }

        if (ticketEdicaoInput.getIdAluno() != null) {
            Aluno aluno = new Aluno();
            aluno.setIdUsuario(ticketEdicaoInput.getIdAluno());
            ticketExistente.setAluno(aluno);
        }

        if (ticketEdicaoInput.getIdStatus() != null) {
            Status status = new Status();
            status.setIdStatus(ticketEdicaoInput.getIdStatus());
            ticketExistente.setStatus(status);
        }

        if (ticketEdicaoInput.getIdPrioridade() != null) {
            Prioridade prioridade = new Prioridade();
            prioridade.setIdPrioridade(ticketEdicaoInput.getIdPrioridade());
            ticketExistente.setPrioridade(prioridade);
        }

        if (ticketEdicaoInput.getIdCategoria() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(ticketEdicaoInput.getIdCategoria());
            ticketExistente.setCategoria(categoria);
        }
    }
}
