package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.BaseConhecimentoNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.exception.CategoriaNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
import br.ufma.glp.unidesk.backend.domain.repository.BaseConhecimentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class BaseConhecimentoService {
    private final CategoriaService categoriaService;
    private final BaseConhecimentoRepository baseConhecimentoRepository;

    public List<BaseConhecimento> listarTodos() {
        return baseConhecimentoRepository.findAll();
    }

    public BaseConhecimento buscarPorIdOuFalhar(Long idBaseConhecimento) {
        return baseConhecimentoRepository.findById(idBaseConhecimento)
                .orElseThrow(() -> new BaseConhecimentoNaoEncontradaException(idBaseConhecimento));
    }

    public BaseConhecimento buscarPorTituloOuFalhar(String titulo) {
        return baseConhecimentoRepository.findByTitulo(titulo)
                .orElseThrow(() -> new BaseConhecimentoNaoEncontradaException("Artigo não encontrado: " + titulo));
    }

    public List<BaseConhecimento> buscarPorCategoria(String nomeCategoria) {
        var categoria = categoriaService.buscarPorNomeOuFalhar(nomeCategoria);
        return baseConhecimentoRepository.findByCategoria(categoria);
    }

    protected boolean baseConhecimentoExistePorId(Long baseConhecimentoId) {
        return !baseConhecimentoRepository.existsById(baseConhecimentoId);
    }

    @Transactional
    public BaseConhecimento salvarBaseConhecimento(BaseConhecimento baseConhecimento) {
        validarAtributos(baseConhecimento);
        var categoriaCompleta = categoriaService.buscarPorIdOuFalhar(baseConhecimento.getCategoria().getIdCategoria());
        baseConhecimento.setCategoria(categoriaCompleta);
        return baseConhecimentoRepository.save(baseConhecimento);
    }

    @Transactional
    public BaseConhecimento atualizarBaseConhecimento(Long idBaseConhecimento, BaseConhecimento baseConhecimento) {
        if (baseConhecimentoExistePorId(idBaseConhecimento)) {
            throw new BaseConhecimentoNaoEncontradaException(idBaseConhecimento);
        }
        baseConhecimento.setIdArtigo(idBaseConhecimento);
        validarAtributos(baseConhecimento);
        var categoriaCompleta = categoriaService.buscarPorIdOuFalhar(baseConhecimento.getCategoria().getIdCategoria());
        baseConhecimento.setCategoria(categoriaCompleta);
        return baseConhecimentoRepository.save(baseConhecimento);
    }

    @Transactional
    public void removerBaseConhecimento(Long idBaseConhecimento) {
        if (baseConhecimentoExistePorId(idBaseConhecimento)) {
            throw new BaseConhecimentoNaoEncontradaException(idBaseConhecimento);
        }
        baseConhecimentoRepository.deleteById(idBaseConhecimento);
    }


    private void validarAtributos(BaseConhecimento baseConhecimento) {
        if (baseConhecimento.getTitulo() == null || baseConhecimento.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título do artigo não pode ser vazio.");
        }
        if (baseConhecimento.getConteudo() == null || baseConhecimento.getConteudo().isBlank()) {
            throw new IllegalArgumentException("O conteúdo do artigo não pode ser vazio.");
        }
        if( baseConhecimento.getCategoria() == null || baseConhecimento.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("A categoria do artigo não pode ser nula.");
        }
        if (!categoriaService.categoriaExistePorId(baseConhecimento.getCategoria().getIdCategoria())) {
            throw new CategoriaNaoEncontradaException("A categoria do artigo não existe.");
        }
    }

}