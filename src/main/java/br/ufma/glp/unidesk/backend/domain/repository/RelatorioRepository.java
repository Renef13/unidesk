package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

    /**
     * Busca um relatório pelo seu ID.
     *
     * @param idRelatorio ID do relatório.
     * @return Relatório com o ID informado.
     */
    Optional<Relatorio> findByIdRelatorio(Long idRelatorio);

    /**
     * Busca relatórios por data específica.
     *
     * @param data Data dos relatórios.
     * @return Lista de relatórios da data informada.
     */
    List<Relatorio> findByData(LocalDate data);

    /**
     * Busca relatórios com data entre o período informado.
     *
     * @param dataInicio Data inicial.
     * @param dataFim Data final.
     * @return Lista de relatórios do período informado.
     */
    List<Relatorio> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca relatórios pelo tipo.
     *
     * @param tipoRelatorio Tipo do relatório.
     * @return Lista de relatórios do tipo informado.
     */
    List<Relatorio> findByTipoRelatorio(String tipoRelatorio);

    /**
     * Busca relatórios que contêm o texto no conteúdo.
     *
     * @param texto Texto a ser buscado no conteúdo.
     * @return Lista de relatórios que contêm o texto no conteúdo.
     */
    List<Relatorio> findByConteudoContainingIgnoreCase(String texto);

    /**
     * Busca relatórios por tipo e data.
     *
     * @param tipoRelatorio Tipo do relatório.
     * @param data Data do relatório.
     * @return Lista de relatórios do tipo e data informados.
     */
    List<Relatorio> findByTipoRelatorioAndData(String tipoRelatorio, LocalDate data);

    /**
     * Busca relatórios com data posterior à informada.
     *
     * @param data Data de referência.
     * @return Lista de relatórios com data posterior à informada.
     */
    List<Relatorio> findByDataAfter(LocalDate data);
}