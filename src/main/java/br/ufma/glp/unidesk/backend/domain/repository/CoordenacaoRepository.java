package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoordenacaoRepository extends JpaRepository<Coordenacao, Long> {

    /**
     * Busca uma coordenação pelo seu nome exato.
     *
     * @param nome Nome da coordenação.
     * @return Coordenação com o nome informado.
     */
    Optional<Coordenacao> findByNome(String nome);

    /**
     * Busca uma coordenação pelo seu ID.
     *
     * @param idCoordenacao ID da coordenação.
     * @return Coordenação com o ID informado.
     */
    Optional<Coordenacao> findByIdCoordenacao(Long idCoordenacao);

    /**
     * Busca coordenações que contêm o texto no nome.
     *
     * @param texto Texto a ser buscado no nome.
     * @return Lista de coordenações que contêm o texto no nome.
     */
    List<Coordenacao> findByNomeContainingIgnoreCase(String texto);

    /**
     * Busca coordenações por curso.
     *
     * @param curso Curso das coordenações.
     * @return Lista de coordenações do curso informado.
     */
    Optional<Coordenacao> findByCurso(Curso curso);

    /**
     * Busca coordenações pelo ID do curso.
     *
     * @param idCurso ID do curso.
     * @return Lista de coordenações do curso informado.
     */
    Optional<Coordenacao> findByCursoIdCurso(Long idCurso);

    /**
     * Verifica se existe uma coordenação com o nome informado.
     *
     * @param nome Nome da coordenação.
     * @return Verdadeiro se a coordenação existir, falso caso contrário.
     */
    boolean existsByNome(String nome);
}