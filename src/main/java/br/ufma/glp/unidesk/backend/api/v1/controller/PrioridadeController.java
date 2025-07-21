package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.PrioridadeCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.PrioridadeEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.PrioridadeModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.PrioridadeCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.PrioridadeEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.PrioridadeModel;
import br.ufma.glp.unidesk.backend.domain.model.Prioridade;
import br.ufma.glp.unidesk.backend.domain.service.PrioridadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/prioridades")
@RequiredArgsConstructor
public class PrioridadeController {

    private final PrioridadeService prioridadeService;
    private final PrioridadeModelAssembler prioridadeModelAssembler;
    private final PrioridadeCadastroInputDisassembler prioridadeCadastroInputDisassembler;
    private final PrioridadeEdicaoInputDisassembler prioridadeEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<PrioridadeModel> listar() {
        return prioridadeModelAssembler.toCollectionModel(prioridadeService.listarTodos());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PrioridadeModel buscarPorId(@PathVariable Long id) {
        return prioridadeModelAssembler.toModel(prioridadeService.buscarPorIdOuFalhar(id));
    }

    @GetMapping("/nivel")
    @ResponseStatus(HttpStatus.OK)
    public PrioridadeModel buscarPorNivel(@RequestParam String nivel) {
        return prioridadeModelAssembler.toModel(prioridadeService.buscarPorNivelOuFalhar(nivel));
    }

    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public List<PrioridadeModel> buscarPorNivelContendo(@RequestParam("texto") String texto) {
        return prioridadeModelAssembler.toCollectionModel(prioridadeService.buscarPorNivelContendo(texto));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public PrioridadeModel adicionar(@RequestBody @Valid PrioridadeCadastroInput prioridadeInput) {
        Prioridade prioridade = prioridadeCadastroInputDisassembler.toDomainObject(prioridadeInput);
        return prioridadeModelAssembler.toModel(prioridadeService.salvar(prioridade));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PrioridadeModel atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PrioridadeEdicaoInput prioridadeInput) {

        Prioridade prioridadeAtual = prioridadeService.buscarPorIdOuFalhar(id);
        prioridadeInput.setIdPrioridade(id);
        prioridadeEdicaoInputDisassembler.copyToDomainObject(prioridadeInput, prioridadeAtual);

        return prioridadeModelAssembler.toModel(prioridadeService.atualizar(prioridadeAtual));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        prioridadeService.excluir(id);
    }
}