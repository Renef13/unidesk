package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Busca um curso pelo seu nome exato.
     *
     * @param nome Nome do curso.
     * @return Curso com o nome informado.
     */
    Optional<Curso> findByNome(String nome);

    /**
     * Busca um curso pelo seu ID.
     *
     * @param idCurso ID do curso.
     * @return Curso com o ID informado.
     */
    Optional<Curso> findByIdCurso(Long idCurso);

    /**
     * Busca cursos que contêm o texto no nome.
     *
     * @param texto Texto a ser buscado no nome.
     * @return Lista de cursos que contêm o texto no nome.
     */
    List<Curso> findByNomeContainingIgnoreCase(String texto);

    /**
     * Busca cursos por campus específico.
     *
     * @param campus Nome do campus.
     * @return Lista de cursos do campus informado.
     */
    List<Curso> findByCampus(String campus);

    /**
     * Busca cursos que contêm o texto no nome do campus.
     *
     * @param texto Texto a ser buscado no campus.
     * @return Lista de cursos que contêm o texto no campus.
     */
    List<Curso> findByCampusContainingIgnoreCase(String texto);

    /**
     * Verifica se existe um curso com o nome informado.
     *
     * @param nome Nome do curso.
     * @return Verdadeiro se o curso existir, falso caso contrário.
     */
    boolean existsByNome(String nome);

    /**
     * Busca cursos por texto no nome ou campus.
     *
     * @param texto Texto a ser buscado.
     * @return Lista de cursos que contêm o texto no nome ou campus.
     */
    List<Curso> findByNomeContainingIgnoreCaseOrCampusContainingIgnoreCase(String texto, String textoCampus);
}