package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param idUsuario ID do usuário.
     * @return Usuário com o ID informado.
     */
    Optional<Usuario> findByIdUsuario(Long idUsuario);

    /**
     * Busca um usuário pelo seu nome de usuário exato.
     *
     * @param usuario Nome de usuário.
     * @return Usuário com o nome de usuário informado.
     */
    Optional<Usuario> findByUsuario(String usuario);

    /**
     * Busca um usuário pelo seu email exato.
     *
     * @param email Email do usuário.
     * @return Usuário com o email informado.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca um usuário pelo seu nome exato.
     *
     * @param nome Nome do usuário.
     * @return Usuário com o nome informado.
     */
    Optional<Usuario> findByNome(String nome);

    /**
     * Busca usuários que contêm o texto no nome.
     *
     * @param texto Texto a ser buscado no nome.
     * @return Lista de usuários que contêm o texto no nome.
     */
    List<Usuario> findByNomeContainingIgnoreCase(String texto);

    /**
     * Verifica se existe um usuário com o email informado.
     *
     * @param email Email do usuário.
     * @return Verdadeiro se o usuário existir, falso caso contrário.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se existe um usuário com o nome informado.
     *
     * @param nome Nome do usuário.
     * @return Verdadeiro se o usuário existir, falso caso contrário.
     */
    boolean existsByNome(String nome);
}