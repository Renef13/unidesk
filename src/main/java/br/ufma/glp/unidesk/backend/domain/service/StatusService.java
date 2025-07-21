package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.StatusEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.StatusNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import br.ufma.glp.unidesk.backend.domain.repository.StatusRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class StatusService {

    private final StatusRepository statusRepository;
    private final TicketRepository ticketRepository;


    public List<Status> listarTodos() {
        return statusRepository.findAll();
    }

    public List<Status> buscarPorNomeContendo(String texto) {
        return statusRepository.findByNomeContainingIgnoreCase(texto);
    }

    public Status buscarPorIdOuFalhar(
            @Valid @NotNull(message = "O ID do status não pode ser nulo")
            @Positive(message = "O ID do status deve ser um número positivo") Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new StatusNaoEncontradoException(id));
    }

    public Status buscarPorNomeOuFalhar(
            @Valid @NotNull(message = "O nome do status não pode ser nulo") String nome) {
        return statusRepository.findByNome(nome)
                .orElseThrow(() -> new StatusNaoEncontradoException("Status com nome " + nome + " não encontrado"));
    }

    @Transactional
    public Status salvar(@Valid @NotNull(message = "O status não pode ser nulo") Status status) {
        validarNomeUnico(status);
        return statusRepository.save(status);
    }

    @Transactional
    public Status atualizar(
            @Valid @NotNull(message = "O status não pode ser nulo") Status status) {
        Status statusAtual = buscarPorIdOuFalhar(status.getIdStatus());

        if (!statusAtual.getNome().equals(status.getNome()) &&
                statusRepository.existsByNome(status.getNome())) {
            throw new StatusEmUsoException("O nome '" + status.getNome() + "' já está em uso por outro status");
        }

        statusAtual.setNome(status.getNome());
        return statusRepository.save(statusAtual);
    }

    @Transactional
    public void excluir(
            @Valid @NotNull(message = "O ID do status não pode ser nulo")
            @Positive(message = "O ID do status deve ser um número positivo") Long id) {
        Status status = buscarPorIdOuFalhar(id);

        verificarEmUso(status);

        statusRepository.delete(status);
    }

    private void validarNomeUnico(Status status) {
        if (statusRepository.existsByNome(status.getNome())) {
            throw new StatusEmUsoException("Já existe um status com o nome '" + status.getNome() + "'");
        }
    }


    private void verificarEmUso(Status status) {
        if (ticketRepository.existsByStatus(status)) {
            throw new StatusEmUsoException(status.getIdStatus());
        }
    }

}