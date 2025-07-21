package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {

    /**
     * Busca um coordenador pela sua matrícula.
     *
     * @param matricula Matrícula do coordenador.
     * @return Coordenador com a matrícula informada.
     */
    Optional<Coordenador> findByMatricula(String matricula);

    /**
     * Busca coordenadores que contêm o texto na matrícula.
     *
     * @param texto Texto a ser buscado na matrícula.
     * @return Lista de coordenadores que contêm o texto na matrícula.
     */
    List<Coordenador> findByMatriculaContainingIgnoreCase(String texto);

    /**
     * Busca coordenadores pelo status de ativo.
     *
     * @param ativo Status de ativo do coordenador.
     * @return Lista de coordenadores com o status informado.
     */
    List<Coordenador> findByAtivo(Boolean ativo);

    /**
     * Busca coordenadores por coordenação.
     *
     * @param coordenacao Coordenação dos coordenadores.
     * @return Lista de coordenadores da coordenação informada.
     */
    List<Coordenador> findByCoordenacao(Coordenacao coordenacao);

    /**
     * Busca coordenadores pelo ID da coordenação.
     *
     * @param idCoordenacao ID da coordenação.
     * @return Lista de coordenadores da coordenação informada.
     */
    List<Coordenador> findByCoordenacaoIdCoordenacao(Long idCoordenacao);

    /**
     * Verifica se existe um coordenador com a matrícula informada.
     *
     * @param matricula Matrícula do coordenador.
     * @return Verdadeiro se o coordenador existir, falso caso contrário.
     */
    boolean existsByMatricula(String matricula);

    /**
     * Busca coordenadores por coordenação e status de ativo.
     *
     * @param coordenacao Coordenação dos coordenadores.
     * @param ativo Status de ativo do coordenador.
     * @return Lista de coordenadores da coordenação e status informados.
     */
    List<Coordenador> findByCoordenacaoAndAtivo(Coordenacao coordenacao, Boolean ativo);

    /**
     * Busca coordenadores pelo ID da coordenação e status de ativo.
     *
     * @param idCoordenacao ID da coordenação.
     * @param ativo Status de ativo do coordenador.
     * @return Lista de coordenadores da coordenação e status informados.
     */
    List<Coordenador> findByCoordenacaoIdCoordenacaoAndAtivo(Long idCoordenacao, Boolean ativo);
}