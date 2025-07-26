package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.TicketCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.TicketEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.TicketModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.TicketCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.TicketEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.TicketModel;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketModelAssembler ticketModelAssembler;
    private final TicketCadastroInputDisassembler ticketCadastroInputDisassembler;
    private final TicketEdicaoInputDisassembler ticketEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Page<TicketModel> listar(@RequestParam int page, @RequestParam int size, @RequestParam Usuario usuario) {
        return ticketService.listarTickets(usuario, page, size)
                .map(ticketModelAssembler::toModel);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketModel buscarPorId(@PathVariable Long id) {
        return ticketModelAssembler.toModel(ticketService.buscarTicketPorId(id));
    }

    @GetMapping("/categoria")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketModel> filtrarPorCategoria(@RequestParam String nomeCategoria) {
        return ticketModelAssembler.toCollectionModel(ticketService.filtrarPorCategoria(nomeCategoria));
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketModel> buscarPorStatus(@RequestParam Usuario usuario, @RequestParam Long idStatus) {
        return ticketModelAssembler.toCollectionModel(ticketService.buscarTicketsPorStatusEspecifico(usuario, idStatus));
    }

    @GetMapping("/periodo")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketModel> buscarPorPeriodo(@RequestParam Instant dataInicio, @RequestParam Instant dataFim) {
        return ticketModelAssembler.toCollectionModel(ticketService.buscarTicketsPorPeriodo(dataInicio, dataFim));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketModel adicionar(@RequestBody @Valid TicketCadastroInput ticketInput, @RequestParam(required = false) MultipartFile file) throws Exception {
        return ticketModelAssembler.toModel(ticketService.novoTicket(ticketCadastroInputDisassembler.toDomainObject(ticketInput), file));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketModel atualizar(@PathVariable Long id, @RequestBody @Valid TicketEdicaoInput ticketEdicaoInput) {
        Ticket ticketExistente = ticketService.buscarTicketPorId(id);
        ticketEdicaoInputDisassembler.copyToDomainObject(ticketEdicaoInput, ticketExistente);
        Ticket ticketAtualizado = ticketService.atualizarTicket(ticketExistente);
        return ticketModelAssembler.toModel(ticketAtualizado);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public TicketModel alterarStatus(@PathVariable Long id, @RequestBody @Valid TicketEdicaoInput ticketEdicaoInput) {
        Ticket ticket = ticketService.buscarTicketPorId(id);
        ticketEdicaoInputDisassembler.copyToDomainObject(ticketEdicaoInput, ticket);
        Ticket ticketAtualizado = ticketService.alterarStatusTicket(id, ticket);
        return ticketModelAssembler.toModel(ticketAtualizado);
    }

    @PatchMapping("/{id}/fechar")
    @ResponseStatus(HttpStatus.OK)
    public TicketModel fecharTicket(@PathVariable Long id) {
        Ticket ticketAtualizado = ticketService.fecharTicket(id);
        return ticketModelAssembler.toModel(ticketAtualizado);
    }


}