package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca uma categoria pelo nome.
     *
     * @param nome Nome da categoria.
     * @return Categoria correspondente, caso exista.
     */
    Categoria findByNome(String nome);

    /**
     * Busca uma categoria pelo nome com retorno Optional.
     *
     * @param nome Nome da categoria.
     * @return Optional contendo a categoria correspondente, caso exista.
     */
    Optional<Categoria> findOptionalByNome(String nome);

    /**
     * Verifica se existe uma categoria com o nome informado.
     *
     * @param nome Nome da categoria.
     * @return Verdadeiro se a categoria existir, falso caso contrário.
     */
    boolean existsByNome(String nome);

    /**
     * Busca uma categoria pelo ID.
     *
     * @param idCategoria ID da categoria.
     * @return Optional contendo a categoria correspondente, caso exista.
     */
    Optional<Categoria> findByIdCategoria(Long idCategoria);


    /**
     * Busca categorias pelo nome exato, ignorando maiúsculas e minúsculas.
     *
     * @param nome Nome exato da categoria.
     * @return Lista de categorias correspondentes.
     */
    List<Categoria> findByNomeIgnoreCase(String nome);

    /**
     * Verifica se existe uma categoria com o nome exato, ignorando maiúsculas e minúsculas.
     *
     * @param nome Nome da categoria a ser verificado.
     * @return true se existir, false caso contrário.
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Busca categorias por ID ordenadas por nome.
     *
     * @param ids Lista de IDs de categorias.
     * @return Lista de categorias ordenadas por nome.
     */
    List<Categoria> findByIdCategoriaInOrderByNomeAsc(List<Long> ids);


}