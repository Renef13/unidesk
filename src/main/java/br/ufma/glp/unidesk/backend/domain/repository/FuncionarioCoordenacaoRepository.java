package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioCoordenacaoRepository extends JpaRepository<FuncionarioCoordenacao, Long> {
}
