package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Categoria;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

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
     */
    List<Ticket> findByAlunoIdUsuario(Long idAluno);

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

}