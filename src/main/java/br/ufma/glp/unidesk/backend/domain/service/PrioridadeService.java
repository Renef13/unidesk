package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.PrioridadeEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.PrioridadeNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.exception.PrioridadeNivelEmUsoException;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import br.ufma.glp.unidesk.backend.domain.repository.PrioridadeRepository;
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
public class PrioridadeService {

    private final PrioridadeRepository prioridadeRepository;
    private final TicketRepository ticketRepository;

    public List<Prioridade> listarTodos() {
        return prioridadeRepository.findAll();
    }

    public List<Prioridade> buscarPorNivelContendo(String texto) {
        return prioridadeRepository.findByNivelContainingIgnoreCase(texto);
    }

    public Prioridade buscarPorIdOuFalhar(
            @Valid @NotNull(message = "O ID da prioridade não pode ser nulo")
            @Positive(message = "O ID da prioridade deve ser um número positivo") Long id) {
        return prioridadeRepository.findById(id)
                .orElseThrow(() -> new PrioridadeNaoEncontradaException(id));
    }

    public Prioridade buscarPorNivelOuFalhar(
            @Valid @NotNull(message = "O nível da prioridade não pode ser nulo") String nivel) {
        return prioridadeRepository.findByNivel(nivel)
                .orElseThrow(() -> new PrioridadeNaoEncontradaException(
                        String.format("Prioridade com nível '%s' não encontrada", nivel)));
    }

    @Transactional
    public Prioridade salvar(@Valid @NotNull(message = "A prioridade não pode ser nula") Prioridade prioridade) {
        validarNivelUnico(prioridade);
        return prioridadeRepository.save(prioridade);
    }

    @Transactional
    public Prioridade atualizar(
            @Valid @NotNull(message = "A prioridade não pode ser nula") Prioridade prioridade) {
        Prioridade prioridadeAtual = buscarPorIdOuFalhar(prioridade.getIdPrioridade());

        if (!prioridadeAtual.getNivel().equals(prioridade.getNivel()) &&
                prioridadeRepository.existsByNivel(prioridade.getNivel())) {
            throw PrioridadeNivelEmUsoException.comNivel(prioridade.getNivel());
        }

        prioridadeAtual.setNivel(prioridade.getNivel());
        return prioridadeRepository.save(prioridadeAtual);
    }

    @Transactional
    public void excluir(
            @Valid @NotNull(message = "O ID da prioridade não pode ser nulo")
            @Positive(message = "O ID da prioridade deve ser um número positivo") Long id) {
        Prioridade prioridade = buscarPorIdOuFalhar(id);

        verificarEmUso(prioridade);

        prioridadeRepository.delete(prioridade);
    }

    private void validarNivelUnico(Prioridade prioridade) {
        if (prioridadeRepository.existsByNivel(prioridade.getNivel())) {
            throw PrioridadeNivelEmUsoException.comNivel(prioridade.getNivel());
        }
    }

    private void verificarEmUso(Prioridade prioridade) {
        if (ticketRepository.existsByPrioridade(prioridade)) {
            throw new PrioridadeEmUsoException(prioridade.getIdPrioridade());
        }
    }
}