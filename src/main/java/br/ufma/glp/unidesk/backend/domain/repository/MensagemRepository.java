package br.ufma.glp.unidesk.backend.domain.repository;

import br.ufma.glp.unidesk.backend.domain.model.Mensagem;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    /**
     * Busca uma mensagem pelo seu ID.
     *
     * @param idMensagem ID da mensagem.
     * @return Mensagem com o ID informado.
     */
    Optional<Mensagem> findByIdMensagem(Long idMensagem);

    /**
     * Busca mensagens que contêm o texto no conteúdo.
     *
     * @param texto Texto a ser buscado no conteúdo.
     * @return Lista de mensagens que contêm o texto no conteúdo.
     */
    List<Mensagem> findByConteudoContainingIgnoreCase(String texto);

    /**
     * Busca mensagens por usuário.
     *
     * @param usuario Usuário das mensagens.
     * @return Lista de mensagens do usuário informado.
     */
    List<Mensagem> findByUsuario(Usuario usuario);

    /**
     * Busca mensagens pelo ID do usuário.
     *
     * @param idUsuario ID do usuário.
     * @return Lista de mensagens do usuário informado.
     */
    List<Mensagem> findByUsuarioIdUsuario(Long idUsuario);

    /**
     * Busca mensagens por ticket.
     *
     * @param ticket Ticket das mensagens.
     * @return Lista de mensagens do ticket informado.
     */
    List<Mensagem> findByTicket(Ticket ticket);

    /**
     * Busca mensagens pelo ID do ticket.
     *
     * @param idTicket ID do ticket.
     * @return Lista de mensagens do ticket informado.
     */
    List<Mensagem> findByTicketIdTicket(Long idTicket);

    /**
     * Busca mensagens por usuário e ticket.
     *
     * @param usuario Usuário das mensagens.
     * @param ticket  Ticket das mensagens.
     * @return Lista de mensagens do usuário e ticket informados.
     */
    List<Mensagem> findByUsuarioAndTicket(Usuario usuario, Ticket ticket);

    /**
     * Busca mensagens por ID do usuário e ID do ticket.
     *
     * @param idUsuario ID do usuário.
     * @param idTicket  ID do ticket.
     * @return Lista de mensagens do usuário e ticket informados.
     */
    List<Mensagem> findByUsuarioIdUsuarioAndTicketIdTicket(Long idUsuario, Long idTicket);
}