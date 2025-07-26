package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.RelatorioCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.RelatorioEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.RelatorioModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.RelatorioCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.RelatorioEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.RelatorioModel;
import br.ufma.glp.unidesk.backend.domain.model.Relatorio;
import br.ufma.glp.unidesk.backend.domain.service.RelatorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final RelatorioModelAssembler relatorioModelAssembler;
    private final RelatorioCadastroInputDisassembler relatorioCadastroInputDisassembler;
    private final RelatorioEdicaoInputDisassembler relatorioEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<RelatorioModel> listar() {
        return relatorioModelAssembler.toCollectionModel(relatorioService.listarTodos());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RelatorioModel buscarPorId(@PathVariable Long id) {
        return relatorioModelAssembler.toModel(relatorioService.buscarPorIdOuFalhar(id));
    }

    @GetMapping("/buscar-por-data")
    @ResponseStatus(HttpStatus.OK)
    public List<RelatorioModel> buscarPorData(@RequestParam LocalDate data) {
        return relatorioModelAssembler.toCollectionModel(relatorioService.buscarPorData(data));
    }

    @GetMapping("/buscar-por-tipo")
    @ResponseStatus(HttpStatus.OK)
    public List<RelatorioModel> buscarPorTipo(@RequestParam String tipo) {
        return relatorioModelAssembler.toCollectionModel(relatorioService.buscarPorTipo(tipo));
    }

    @GetMapping("/buscar-por-conteudo")
    @ResponseStatus(HttpStatus.OK)
    public List<RelatorioModel> buscarPorConteudo(@RequestParam String texto) {
        return relatorioModelAssembler.toCollectionModel(relatorioService.buscarPorConteudo(texto));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public RelatorioModel adicionar(@RequestBody @Valid RelatorioCadastroInput relatorioInput) {
        return relatorioModelAssembler.toModel(
                relatorioService.salvar(relatorioCadastroInputDisassembler.toDomainObject(relatorioInput))
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RelatorioModel atualizar(@PathVariable Long id, @RequestBody @Valid RelatorioEdicaoInput relatorioEdicaoInput) {
        Relatorio relatorioExistente = relatorioService.buscarPorIdOuFalhar(id);
        relatorioEdicaoInputDisassembler.copyToDomainObject(relatorioEdicaoInput, relatorioExistente);
        return relatorioModelAssembler.toModel(relatorioService.atualizar(relatorioExistente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        relatorioService.remover(id);
    }
}