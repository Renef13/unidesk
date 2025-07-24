package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.RelatorioEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.RelatorioInvalidoException;
import br.ufma.glp.unidesk.backend.domain.exception.RelatorioNaoEncontradoException;
import br.ufma.glp.unidesk.backend.domain.model.Relatorio;
import br.ufma.glp.unidesk.backend.domain.repository.RelatorioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;

    public List<Relatorio> listarTodos() {
        return relatorioRepository.findAll();
    }

    public Relatorio buscarPorIdOuFalhar(@NotNull Long idRelatorio) {
        return relatorioRepository.findByIdRelatorio(idRelatorio)
                .orElseThrow(() -> new RelatorioNaoEncontradoException(idRelatorio));
    }

    public List<Relatorio> buscarPorData(@NotNull LocalDate data) {
        return relatorioRepository.findByData(data);
    }

    public List<Relatorio> buscarPorTipo(@NotNull String tipoRelatorio) {
        return relatorioRepository.findByTipoRelatorio(tipoRelatorio);
    }

    public List<Relatorio> buscarPorConteudo(String texto) {
        return relatorioRepository.findByConteudoContainingIgnoreCase(texto);
    }

    @Transactional
    public Relatorio salvar(@Valid @NotNull Relatorio relatorio) {
        validarRelatorio(relatorio);
        return relatorioRepository.save(relatorio);
    }

    @Transactional
    public Relatorio atualizar(@NotNull Long idRelatorio, @Valid @NotNull Relatorio relatorio) {
        Relatorio existente = buscarPorIdOuFalhar(idRelatorio);
        validarRelatorio(relatorio);

        existente.setConteudo(relatorio.getConteudo());
        existente.setData(relatorio.getData());
        existente.setTipoRelatorio(relatorio.getTipoRelatorio());

        return relatorioRepository.save(existente);
    }

    @Transactional
    public void remover(@NotNull Long idRelatorio) {
        Relatorio relatorio = buscarPorIdOuFalhar(idRelatorio);
        try {
            relatorioRepository.delete(relatorio);
            relatorioRepository.flush();
        } catch (Exception e) {
            throw new RelatorioEmUsoException(idRelatorio);
        }
    }

    private void validarRelatorio(Relatorio relatorio) {
        if (relatorio.getConteudo() == null || relatorio.getConteudo().isBlank()) {
            throw new RelatorioInvalidoException("Conteúdo do relatório não pode ser vazio");
        }
        if (relatorio.getData() == null) {
            throw new RelatorioInvalidoException("Data do relatório não pode ser nula");
        }
        if (relatorio.getTipoRelatorio() == null || relatorio.getTipoRelatorio().isBlank()) {
            throw new RelatorioInvalidoException("Tipo do relatório não pode ser vazio");
        }
    }
}