package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.TicketEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEdicaoInputDisassembler {

    private final ModelMapper modelMapper;

    public void copyToDomainObject(@Valid TicketEdicaoInput ticketEdicaoInput, Ticket ticketExistente) {
        modelMapper.map(ticketEdicaoInput, ticketExistente);

        if (ticketEdicaoInput.getIdCoordenacao() != null) {
            Coordenacao coordenacao = new Coordenacao();
            coordenacao.setIdCoordenacao(ticketEdicaoInput.getIdCoordenacao());
            ticketExistente.setCoordenacao(coordenacao);
        } else {
            ticketExistente.setCoordenacao(null);
        }

        if (ticketEdicaoInput.getIdFuncionario() != null) {
            FuncionarioCoordenacao funcionario = new FuncionarioCoordenacao();
            funcionario.setIdUsuario(ticketEdicaoInput.getIdFuncionario());
            ticketExistente.setFuncionario(funcionario);
        } else {
            ticketExistente.setFuncionario(null);
        }

        if (ticketEdicaoInput.getIdAluno() != null) {
            Aluno aluno = new Aluno();
            aluno.setIdUsuario(ticketEdicaoInput.getIdAluno());
            ticketExistente.setAluno(aluno);
        } else {
            ticketExistente.setAluno(null);
        }

        if (ticketEdicaoInput.getIdStatus() != null) {
            Status status = new Status();
            status.setIdStatus(ticketEdicaoInput.getIdStatus());
            ticketExistente.setStatus(status);
        } else {
            ticketExistente.setStatus(null);
        }

        if (ticketEdicaoInput.getIdPrioridade() != null) {
            Prioridade prioridade = new Prioridade();
            prioridade.setIdPrioridade(ticketEdicaoInput.getIdPrioridade());
            ticketExistente.setPrioridade(prioridade);
        } else {
            ticketExistente.setPrioridade(null);
        }

        if (ticketEdicaoInput.getIdCategoria() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(ticketEdicaoInput.getIdCategoria());
            ticketExistente.setCategoria(categoria);
        } else {
            ticketExistente.setCategoria(null);
        }
    }
}
