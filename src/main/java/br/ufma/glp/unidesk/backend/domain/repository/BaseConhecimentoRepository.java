package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseConhecimentoRepository extends JpaRepository<BaseConhecimento, Long> {

    /**
     * Busca um artigo pelo seu título exato.
     *
     * @param titulo Título do artigo.
     * @return Artigo com o título informado.
     */
    Optional<BaseConhecimento> findByTitulo(String titulo);

    /**
     * Busca um artigo pelo seu ID.
     *
     * @param idArtigo ID do artigo.
     * @return Artigo com o ID informado.
     */
    Optional<BaseConhecimento> findByIdArtigo(Long idArtigo);

    /**
     * Busca artigos que contêm o texto no título.
     *
     * @param texto Texto a ser buscado no título.
     * @return Lista de artigos que contêm o texto no título.
     */
    List<BaseConhecimento> findByTituloContainingIgnoreCase(String texto);

    /**
     * Busca artigos por categoria.
     *
     * @param categoria Categoria dos artigos.
     * @return Lista de artigos da categoria informada.
     */
    List<BaseConhecimento> findByCategoria(Categoria categoria);

    /**
     * Busca artigos pelo ID da categoria.
     *
     * @param idCategoria ID da categoria.
     * @return Lista de artigos da categoria informada.
     */
    List<BaseConhecimento> findByCategoriaIdCategoria(Long idCategoria);

    /**
     * Verifica se existe um artigo com o título informado.
     *
     * @param titulo Título do artigo.
     * @return Verdadeiro se o artigo existir, falso caso contrário.
     */
    boolean existsByTitulo(String titulo);

    /**
     * Busca artigos que contêm o texto no conteúdo.
     *
     * @param texto Texto a ser buscado no conteúdo.
     * @return Lista de artigos que contêm o texto no conteúdo.
     * TODO: Verificar se é necessário usar esse.
     */
    List<BaseConhecimento> findByConteudoContainingIgnoreCase(String texto);

    /**
     * Busca artigos por texto no título ou conteúdo.
     *
     * @param texto Texto a ser buscado.
     * @return Lista de artigos que contêm o texto no título ou conteúdo.
     * TODO: Verificar se é necessário usar esse.
     */
    List<BaseConhecimento> findByTituloContainingIgnoreCaseOrConteudoContainingIgnoreCase(String texto, String textoConteudo);
}