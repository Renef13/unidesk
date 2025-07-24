package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.BaseConhecimentoModel;
import br.ufma.glp.unidesk.backend.domain.service.BaseConhecimentoService;
import br.ufma.glp.unidesk.backend.domain.service.FuncionarioCoordenacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/faq")
public class BaseConhecimentoController {

    private final BaseConhecimentoService baseConhecimentoService;
    private final BaseConhecimentoModelAssembler baseConhecimentoModelAssembler;
    private final BaseConhecimentoCadastroInputDisassembler baseConhecimentoCadastroInputDisassembler;
    private final BaseConhecimentoEdicaoInputDisassembler baseConhecimentoEdicaoInputDisassembler;
    private final FuncionarioCoordenacaoService funcionarioCoordenacaoService;


    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<BaseConhecimentoModel> listar() {
        return baseConhecimentoModelAssembler.toCollectionModel(baseConhecimentoService.listarTodos());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseConhecimentoModel buscarPorId(@PathVariable Long id) {
        return baseConhecimentoModelAssembler.toModel(baseConhecimentoService.buscarPorIdOuFalhar(id));
    }

    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public BaseConhecimentoModel buscarPorNome(@RequestParam String nome) {
        return baseConhecimentoModelAssembler.toModel(baseConhecimentoService.buscarPorTituloOuFalhar(nome));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseConhecimentoModel criarFaq(@RequestBody @Valid BaseConhecimentoCadastroInput baseConhecimentoCadastroInput) {
        return baseConhecimentoModelAssembler.toModel(
                baseConhecimentoService.salvarBaseConhecimento(baseConhecimentoCadastroInputDisassembler.toDomainObject(baseConhecimentoCadastroInput))
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseConhecimentoModel editarFaq(@RequestBody @Valid BaseConhecimentoEdicaoInput baseConhecimentoEdicaoInput, @PathVariable Long id) {
        return baseConhecimentoModelAssembler.toModel(
                baseConhecimentoService.atualizarBaseConhecimento(id, baseConhecimentoEdicaoInputDisassembler.toDomainObject(baseConhecimentoEdicaoInput))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarFaq(@PathVariable Long id) {
        baseConhecimentoService.removerBaseConhecimento(id);
    }
}