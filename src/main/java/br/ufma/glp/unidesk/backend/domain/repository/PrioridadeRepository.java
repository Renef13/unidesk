package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrioridadeRepository extends JpaRepository<Prioridade, Long> {

    /**
     * Busca uma prioridade pelo seu nível exato.
     *
     * @param nivel Nível da prioridade.
     * @return Prioridade com o nível informado.
     */
    Optional<Prioridade> findByNivel(String nivel);

    /**
     * Busca uma prioridade pelo seu ID.
     *
     * @param idPrioridade ID da prioridade.
     * @return Prioridade com o ID informado.
     */
    Optional<Prioridade> findByIdPrioridade(Long idPrioridade);

    /**
     * Busca prioridades que contêm o texto no nível.
     *
     * @param texto Texto a ser buscado no nível.
     * @return Lista de prioridades que contêm o texto no nível.
     */
    List<Prioridade> findByNivelContainingIgnoreCase(String texto);

    /**
     * Verifica se existe uma prioridade com o nível informado.
     *
     * @param nivel Nível da prioridade.
     * @return Verdadeiro se a prioridade existir, falso caso contrário.
     */
    boolean existsByNivel(String nivel);
}