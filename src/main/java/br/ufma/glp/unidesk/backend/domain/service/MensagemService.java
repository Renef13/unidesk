package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.exception.MensagemNaoEncontradaException;
import br.ufma.glp.unidesk.backend.domain.model.Mensagem;
import br.ufma.glp.unidesk.backend.domain.repository.MensagemRepository;
import br.ufma.glp.unidesk.backend.domain.repository.TicketRepository;
import br.ufma.glp.unidesk.backend.domain.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final TicketRepository ticketRepository;

    public List<Mensagem> listarTodas() {
        return mensagemRepository.findAll();
    }

    public Mensagem buscarPorIdOuFalhar(@NotNull Long idMensagem) {
        return mensagemRepository.findById(idMensagem)
                .orElseThrow(() -> new MensagemNaoEncontradaException(idMensagem));
    }

    @Transactional
    public Mensagem salvar(@Valid @NotNull Mensagem mensagem) {
        validarMensagem(mensagem);
        return mensagemRepository.save(mensagem);
    }

    @Transactional
    public Mensagem atualizar(@NotNull Long idMensagem, @Valid @NotNull Mensagem mensagem) {
        Mensagem existente = buscarPorIdOuFalhar(idMensagem);
        validarMensagem(mensagem);

        existente.setConteudo(mensagem.getConteudo());
        existente.setUsuario(mensagem.getUsuario());
        existente.setTicket(mensagem.getTicket());

        return mensagemRepository.save(existente);
    }

    @Transactional
    public void remover(@NotNull Long idMensagem) {
        Mensagem mensagem = buscarPorIdOuFalhar(idMensagem);
        mensagemRepository.delete(mensagem);
    }

    private void validarMensagem(Mensagem mensagem) {
        if (mensagem.getConteudo() == null || mensagem.getConteudo().isBlank()) {
            throw new IllegalArgumentException("Conteúdo da mensagem não pode ser vazio");
        }
        if (mensagem.getTicket() == null || mensagem.getTicket().getIdTicket() == null) {
            throw new IllegalArgumentException("Ticket inválido");
        }
        if (!ticketRepository.existsById(mensagem.getTicket().getIdTicket())) {
            throw new IllegalArgumentException("Ticket associado não existe");
        }
        if (mensagem.getUsuario() == null || mensagem.getUsuario().getIdUsuario() == null) {
            throw new IllegalArgumentException("Usuário inválido");
        }
        if (!usuarioRepository.existsById(mensagem.getUsuario().getIdUsuario())) {
            throw new IllegalArgumentException("Usuário associado não existe");
        }
    }
}