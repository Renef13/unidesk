package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecs {

    public static Specification<Ticket> hasText(String text) {
        return (root, query, cb) -> {
            String pattern = "%" + text.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("titulo")), pattern),
                cb.like(cb.lower(root.get("descricao")), pattern)
            );
        };
    }

    public static Specification<Ticket> hasStatus(Long statusId) {
        return (root, query, cb) ->
            cb.equal(root.get("status").get("idStatus"), statusId);
    }

    public static Specification<Ticket> hasPrioridade(Long prioridadeId) {
        return (root, query, cb) ->
            cb.equal(root.get("prioridade").get("idPrioridade"), prioridadeId);
    }

    public static Specification<Ticket> hasCurso(Long cursoId) {
        return (root, query, cb) ->
            cb.equal(root.get("aluno").get("curso").get("idCurso"), cursoId);
    }

    public static Specification<Ticket> hasAluno(Long alunoId) {
        return (root, query, cb) ->
            cb.equal(root.get("aluno").get("idUsuario"), alunoId);
    }

    public static Specification<Ticket> hasCoordenacao(Long coordenacaoId) {
        return (root, query, cb) ->
            cb.equal(root.get("coordenacao").get("idCoordenacao"), coordenacaoId);
    }

    public static Specification<Ticket> hasFuncionario(Long funcionarioId) {
        return (root, query, cb) ->
            cb.equal(root.get("funcionario").get("idUsuario"), funcionarioId);
    }
}
