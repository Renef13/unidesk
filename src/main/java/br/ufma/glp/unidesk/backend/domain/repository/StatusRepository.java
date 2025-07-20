package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    /**
     * Busca um status pelo seu nome exato.
     *
     * @param nome Nome do status.
     * @return Status com o nome informado.
     */
    Optional<Status> findByNome(String nome);

    /**
     * Busca um status pelo seu ID.
     *
     * @param idStatus ID do status.
     * @return Status com o ID informado.
     */
    Optional<Status> findByIdStatus(Long idStatus);

    /**
     * Busca status que contêm o texto no nome.
     *
     * @param texto Texto a ser buscado no nome.
     * @return Lista de status que contêm o texto no nome.
     */
    List<Status> findByNomeContainingIgnoreCase(String texto);

    /**
     * Verifica se existe um status com o nome informado.
     *
     * @param nome Nome do status.
     * @return Verdadeiro se o status existir, falso caso contrário.
     */
    boolean existsByNome(String nome);
}