package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.StatusCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.StatusEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.StatusModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.StatusCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.StatusEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.StatusModel;
import br.ufma.glp.unidesk.backend.domain.model.Status;
import br.ufma.glp.unidesk.backend.domain.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize( "hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/status")
public class StatusController {

    private final StatusService statusService;
    private final StatusModelAssembler statusModelAssembler;
    private final StatusCadastroInputDisassembler statusCadastroInputDisassembler;
    private final StatusEdicaoInputDisassembler statusEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<StatusModel> listar() {
        return statusModelAssembler.toCollectionModel(statusService.listarTodos());
    }

    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public List<StatusModel> buscarPorNome(@RequestParam("nome") String nome) {
        return statusModelAssembler.toCollectionModel(statusService.buscarPorNomeContendo(nome));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusModel buscarPorId(@PathVariable Long id) {
        return statusModelAssembler.toModel(
                statusService.buscarPorIdOuFalhar(id));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public StatusModel adicionar(@RequestBody @Valid StatusCadastroInput statusInput) {
        return statusModelAssembler.toModel(
                statusService.salvar(statusCadastroInputDisassembler.toDomainObject(statusInput)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusModel atualizar(@PathVariable Long id,@RequestBody @Valid StatusEdicaoInput statusInput) {
        Status statusAtual = statusService.buscarPorIdOuFalhar(id);
        statusEdicaoInputDisassembler.copyToDomainObject(statusInput, statusAtual);
        return statusModelAssembler.toModel(statusService.atualizar(statusAtual));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        statusService.excluir(id);
    }
}