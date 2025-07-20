package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioCoordenacaoRepository extends JpaRepository<FuncionarioCoordenacao, Long> {

    /**
     * Busca um funcionário pela matrícula.
     *
     * @param matricula Matrícula do funcionário.
     * @return Funcionário com a matrícula informada.
     */
    Optional<FuncionarioCoordenacao> findByMatricula(String matricula);

    /**
     * Busca funcionários por coordenação.
     *
     * @param coordenacao Coordenação dos funcionários.
     * @return Lista de funcionários da coordenação.
     */
    List<FuncionarioCoordenacao> findByCoordenacao(Coordenacao coordenacao);

    /**
     * Busca funcionários pelo ID da coordenação.
     *
     * @param idCoordenacao ID da coordenação.
     * @return Lista de funcionários da coordenação.
     */
    List<FuncionarioCoordenacao> findByCoordenacaoIdCoordenacao(Long idCoordenacao);

    /**
     * Verifica se existe um funcionário com a matrícula informada.
     *
     * @param matricula Matrícula do funcionário.
     * @return Verdadeiro se o funcionário existir, falso caso contrário.
     */
    boolean existsByMatricula(String matricula);

    /**
     * Busca funcionários que contêm o texto na matrícula.
     *
     * @param texto Texto a ser buscado na matrícula.
     * @return Lista de funcionários que contêm o texto na matrícula.
     */
    List<FuncionarioCoordenacao> findByMatriculaContainingIgnoreCase(String texto);

    /**
     * Busca funcionários por parte do nome.
     *
     * @param nome Parte do nome a ser buscado.
     * @return Lista de funcionários que contêm o texto no nome.
     */
    List<FuncionarioCoordenacao> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca funcionários por email.
     *
     * @param email Email do funcionário.
     * @return Funcionário com o email informado.
     */
    Optional<FuncionarioCoordenacao> findByEmail(String email);
}