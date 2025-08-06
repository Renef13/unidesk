package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import br.ufma.glp.unidesk.backend.domain.model.Status;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    /**
     * Busca um ticket pelo seu ID.
     */
    Optional<Ticket> findByIdTicket(Long idTicket);

    /**
     * Busca tickets pelo título exato.
     */
    List<Ticket> findByTitulo(String titulo);

    /**
     * Busca tickets que contêm o texto no título.
     */
    List<Ticket> findByTituloContainingIgnoreCase(String texto);

    /**
     * Busca tickets por status.
     */
    List<Ticket> findByStatus(Status status);

    /**
     * Busca tickets pelo ID do status.
     */
    List<Ticket> findByStatusIdStatus(Long idStatus);

    /**
     * Busca tickets por prioridade.
     */
    List<Ticket> findByPrioridade(Prioridade prioridade);

    /**
     * Busca tickets pelo ID da prioridade.
     */
    List<Ticket> findByPrioridadeIdPrioridade(Long idPrioridade);

    /**
     * Busca tickets por categoria.
     */
    List<Ticket> findByCategoria(Categoria categoria);

    /**
     * Busca tickets pelo ID da categoria.
     */
    List<Ticket> findByCategoriaIdCategoria(Long idCategoria);

    /**
     * Busca tickets por aluno.
     */
    List<Ticket> findByAluno(Aluno aluno);

    /**
     * Busca tickets pelo ID do aluno.
     *
     * @param pageable
     */
    Page<Ticket> findByAlunoIdUsuario(Long idAluno, Pageable pageable);

    /**
     * Busca tickets por coordenação paginado.
     */
    Page<Ticket> findByCoordenacaoIdCoordenacao(Long idCoordenacao, Pageable pageable);

    /**
     * Busca tickets por funcionário paginado.
     */
    Page<Ticket> findByFuncionarioIdUsuario(Long idFuncionario, Pageable pageable);

    /**
     * Busca tickets por funcionário.
     */
    List<Ticket> findByFuncionario(FuncionarioCoordenacao funcionario);

    /**
     * Busca tickets pelo ID do funcionário.
     */
    List<Ticket> findByFuncionarioIdUsuario(Long idFuncionario);

    /**
     * Busca tickets por coordenação.
     */
    List<Ticket> findByCoordenacao(Coordenacao coordenacao);

    /**
     * Busca tickets pelo ID da coordenação.
     */
    List<Ticket> findByCoordenacaoIdCoordenacao(Long idCoordenacao);

    /**
     * Busca tickets criados após a data informada.
     */
    List<Ticket> findByDataCriacaoAfter(Instant data);

    /**
     * Busca tickets criados antes da data informada.
     */
    List<Ticket> findByDataCriacaoBefore(Instant data);

    /**
     * Busca tickets criados entre as datas informadas.
     */
    List<Ticket> findByDataCriacaoBetween(Instant dataInicio, Instant dataFim);

    /**
     * Busca tickets fechados após a data informada.
     */
    List<Ticket> findByDataFechamentoAfter(Instant data);

    /**
     * Busca tickets fechados antes da data informada.
     */
    List<Ticket> findByDataFechamentoBefore(Instant data);

    /**
     * Busca tickets fechados entre as datas informadas.
     */
    List<Ticket> findByDataFechamentoBetween(Instant dataInicio, Instant dataFim);

    /**
     * Busca tickets abertos (sem data de fechamento).
     */
    List<Ticket> findByDataFechamentoIsNull();

    /**
     * Busca tickets fechados (com data de fechamento).
     */
    List<Ticket> findByDataFechamentoIsNotNull();

    /**
     * Busca tickets por categoria e status.
     */
    List<Ticket> findByCategoriaAndStatus(Categoria categoria, Status status);

    /**
     * Busca tickets por prioridade e status.
     */
    List<Ticket> findByPrioridadeAndStatus(Prioridade prioridade, Status status);

    /**
     * Busca tickets por aluno e status.
     */
    List<Ticket> findByAlunoAndStatus(Aluno aluno, Status status);

    /**
     * Busca tickets por funcionário e status.
     */
    List<Ticket> findByFuncionarioAndStatus(FuncionarioCoordenacao funcionario, Status status);


    /**
     * Verifica se existem tickets com o status informado.
     *
     * @param status Status para verificação.
     * @return Verdadeiro se existirem tickets com o status, falso caso contrário.
     */
    boolean existsByStatus(Status status);

    /**
     * Verifica se existem tickets com a prioridade informada.
     *
     * @param prioridade Prioridade para verificação.
     * @return Verdadeiro se existirem tickets com a prioridade, falso caso contrário.
     */
    boolean existsByPrioridade(Prioridade prioridade);

    /**
     * Verifica se existem tickets com a categoria informada.
     *
     * @param categoria Categoria para verificação.
     * @return Verdadeiro se existirem tickets com a categoria, falso caso contrário.
     */
    boolean existsByCategoria(Categoria categoria);

    /**
     * Verifica se existem tickets com o aluno informado.
     *
     * @param aluno Aluno para verificação.
     * @return Verdadeiro se existirem tickets com o aluno, falso caso contrário.
     */
    boolean existsByAluno(Aluno aluno);

    /**
     * Recupera todos os tickets associados a uma categoria cujo nome corresponde ao valor informado, ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param nomeCategoria Nome da categoria a ser buscada (case-insensitive).
     * @return Lista de tickets pertencentes à categoria especificada.
     */

    List<Ticket> findByCategoriaNomeIgnoreCase(@NotNull String nomeCategoria);
}