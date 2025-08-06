package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    /**
     * Busca um aluno pelo seu nome.
     *
     * @param nome Nome do aluno.
     * @return Aluno com o nome informado.
     */
    Optional<Aluno> findByNome(String nome);

    /**
     * Busca um aluno pelo seu email.
     *
     * @param email Email do aluno.
     * @return Aluno com o email informado.
     */
    Optional<Aluno> findByEmail(String email);

    /**
     * Busca um aluno pelo seu número de matrícula.
     *
     * @param matricula Número de matrícula do aluno.
     */
    Optional<Aluno> findByMatricula(String matricula);

    /**
     * Verifica se existe um aluno com o email informado ou número de matrícula.
     *
     */
    boolean existsByEmailOrMatricula(String email, String matricula);

    /**
     * Busca alunos pelo curso passado ao parâmetro.
     *
     * @param curso Curso do aluno.
     * @return Lista de alunos que pertencem ao curso informado.
     */
    List<Aluno> findByCurso(Curso curso);

}
