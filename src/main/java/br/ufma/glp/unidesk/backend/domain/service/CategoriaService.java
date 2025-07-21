package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.CategoriaEmUsoException;
import br.ufma.glp.unidesk.backend.domain.exception.CategoriaNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import br.ufma.glp.unidesk.backend.domain.repository.CategoriaRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final TicketRepository ticketRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> buscarPorNomeContendo(String nome) {
        return categoriaRepository.findByNomeIgnoreCase(nome);
    }

    public Categoria buscarPorIdOuFalhar(Long idCategoria) {
        return categoriaRepository.findByIdCategoria(idCategoria)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(idCategoria));
    }

    public Categoria buscarPorNomeOuFalhar(String nome) {
        return categoriaRepository.findOptionalByNome(nome)
                .orElseThrow(() -> new CategoriaNaoEncontradaException(nome));
    }

    public boolean existePorNome(String nome) {
        return categoriaRepository.existsByNomeIgnoreCase(nome);
    }

    @Transactional
    public Categoria salvar(@Valid @NotNull(message = "A categoria não pode ser nula") Categoria categoria) {
        validarNomeCategoriaEmUso(categoria.getNome());
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria atualizar(@Valid @NotNull(message = "A categoria não pode ser nula") Categoria categoria) {
        Categoria categoriaAtual = buscarPorIdOuFalhar(categoria.getIdCategoria());

        if (!categoriaAtual.getNome().equalsIgnoreCase(categoria.getNome())) {
            validarNomeCategoriaEmUso(categoria.getNome());
        }

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluir(
            @Valid @NotNull(message = "O ID da categoria não pode ser nulo")
            @Positive(message = "O ID deve ser um número positivo") Long idCategoria) {
        Categoria categoria = buscarPorIdOuFalhar(idCategoria);

        verificarEmUso(categoria);

        categoriaRepository.delete(categoria);
    }

    private void validarNomeCategoriaEmUso(String nome) {
        if (categoriaRepository.existsByNomeIgnoreCase(nome)) {
            throw new CategoriaEmUsoException("Nome de categoria já está em uso: " + nome);
        }
    }

    private void verificarEmUso(Categoria categoria) {
        if (ticketRepository.existsByCategoria(categoria)) {
            throw new CategoriaEmUsoException(categoria.getIdCategoria());
        }
    }


}
