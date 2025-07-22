package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.MensagemCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Mensagem;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MensagemCadastroInputDisassembler {
    private final ModelMapper modelMapper;

    public Mensagem toDomainObject(MensagemCadastroInput mensagemCadastroInput) {
        Mensagem mensagem = modelMapper.map(mensagemCadastroInput, Mensagem.class);

        if (mensagemCadastroInput.getIdTicket() != null) {
            Ticket ticket = new Ticket();
            ticket.setIdTicket(mensagemCadastroInput.getIdTicket());
            mensagem.setTicket(ticket);
        }
        if (mensagemCadastroInput.getIdUsuario() != null) {
            Aluno aluno = new Aluno();
            aluno.setIdUsuario(mensagemCadastroInput.getIdUsuario());
            mensagem.setUsuario(aluno);
        }
        return mensagem;
    }
}
