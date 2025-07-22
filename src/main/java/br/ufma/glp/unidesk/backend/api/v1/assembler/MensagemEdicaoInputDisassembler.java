package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.MensagemEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Mensagem;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MensagemEdicaoInputDisassembler {
    private final ModelMapper modelMapper;

    public Mensagem toDomainObject(MensagemEdicaoInput mensagemEdicaoInput) {
        Mensagem mensagem = modelMapper.map(mensagemEdicaoInput, Mensagem.class);

        if (mensagemEdicaoInput.getIdTicket() != null) {
            Ticket ticket = new Ticket();
            ticket.setIdTicket(mensagemEdicaoInput.getIdTicket());
            mensagem.setTicket(ticket);
        }
        if (mensagemEdicaoInput.getIdUsuario() != null) {
            Aluno aluno = new Aluno();
            aluno.setIdUsuario(mensagemEdicaoInput.getIdUsuario());
            mensagem.setUsuario(aluno);
        }
        return mensagem;
    }
}
