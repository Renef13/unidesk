package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.MensagemEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Mensagem;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MensagemEdicaoInputDisassembler {
    private final ModelMapper modelMapper;

    public void copyToDomainObject(@Valid MensagemEdicaoInput mensagemEdicaoInput, Mensagem mensagemExistente) {
        modelMapper.map(mensagemEdicaoInput, mensagemExistente);

        if (mensagemEdicaoInput.getIdTicket() != null) {
            Ticket ticket = new Ticket();
            ticket.setIdTicket(mensagemEdicaoInput.getIdTicket());
            mensagemExistente.setTicket(ticket);
        }
        if (mensagemEdicaoInput.getIdUsuario() != null) {
            Aluno aluno = new Aluno();
            aluno.setIdUsuario(mensagemEdicaoInput.getIdUsuario());
            mensagemExistente.setUsuario(aluno);
        }
    }
}
