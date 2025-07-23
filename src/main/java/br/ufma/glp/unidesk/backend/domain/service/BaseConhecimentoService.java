package br.ufma.glp.unidesk.backend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ufma.glp.unidesk.backend.domain.exception.BaseConhecimentoNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.repository.BaseConhecimentoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class BaseConhecimentoService {
    
    private BaseConhecimentoRepository baseConhecimentoRepository;

    @Transactional
    public BaseConhecimento criarFaq(@Valid @NotNull BaseConhecimento base) {
        return baseConhecimentoRepository.save(base);
    }

    public BaseConhecimento buscarFaqPorId(@Valid @NotNull(message = "O id nao deve ser nulo") Long idBase) {
        return baseConhecimentoRepository.findById(idBase).orElseThrow(() -> new BaseConhecimentoNaoEncontradaException(idBase));
    }

    public List<BaseConhecimento> buscarPorNome(@Valid @NotNull(message = "Deve ser preenchido o nome") String nomeBase) {
        List<BaseConhecimento> baseBuscada = baseConhecimentoRepository.findByTituloContainingIgnoreCase(nomeBase);
        if(baseBuscada.isEmpty()) {
            throw new BaseConhecimentoNaoEncontradaException("Nenhuma base encontrada com o texto = " + nomeBase);
        } else {
            return baseBuscada.stream().toList();
        }
    }

    public List<BaseConhecimento> listarFaqs() {
        return baseConhecimentoRepository.findAll();
    }

    @Transactional
    public BaseConhecimento alterarFaq(Long idBase, @Valid @NotNull(message = "É necessario preencher os dados") BaseConhecimento base ) {
        BaseConhecimento baseNova = baseConhecimentoRepository.findById(idBase).orElseThrow(() -> new BaseConhecimentoNaoEncontradaException(idBase));
        
        if(base.getCategoria() != null) {
            baseNova.setCategoria(base.getCategoria());
        }
        if(base.getConteudo() != null) {
            baseNova.setConteudo(base.getConteudo());
        }
        if(base.getTitulo() != null) {
            baseNova.setTitulo(base.getTitulo());
        }
        return baseNova;
    }

    @Transactional
    public void deletarFaq(Usuario usuario, @Valid @NotNull(message = "O id nao deve ser nulo") Long idBase) {
        boolean isAdmin = usuario.getAuthorities().stream().anyMatch(role -> role.getAuthority().contains("ADMIN"));

        if(isAdmin) {
            BaseConhecimento baseParaDeletar = baseConhecimentoRepository.findById(idBase).orElseThrow(() -> new BaseConhecimentoNaoEncontradaException(idBase));
            baseConhecimentoRepository.delete(baseParaDeletar);
        }
    }



}
