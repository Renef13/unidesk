package br.ufma.glp.unidesk.backend.api.v1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.BaseConhecimentoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.BaseConhecimentoModel;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.service.BaseConhecimentoService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/faq")
public class BaseConhecimentoController {
    
    private BaseConhecimentoService baseConhecimentoService;
    private BaseConhecimentoModelAssembler baseConhecimentoModelAssembler;
    private BaseConhecimentoCadastroInputDisassembler baseConhecimentoCadastroInputDisassembler;
    private BaseConhecimentoEdicaoInputDisassembler baseConhecimentoEdicaoInputDisassembler;

    public BaseConhecimentoController(BaseConhecimentoService baseConhecimentoService, BaseConhecimentoModelAssembler baseConhecimentoModelAssembler, BaseConhecimentoCadastroInputDisassembler baseConhecimentoCadastroInputDisassembler, BaseConhecimentoEdicaoInputDisassembler baseConhecimentoEdicaoInputDisassembler) {
        this.baseConhecimentoService = baseConhecimentoService;
        this.baseConhecimentoModelAssembler = baseConhecimentoModelAssembler;
        this.baseConhecimentoCadastroInputDisassembler = baseConhecimentoCadastroInputDisassembler;
        this.baseConhecimentoEdicaoInputDisassembler = baseConhecimentoEdicaoInputDisassembler;
    }

    @GetMapping
    public ResponseEntity<List<BaseConhecimentoModel>> listar() {

        return ResponseEntity.ok().body(baseConhecimentoModelAssembler.toCollectionModel(baseConhecimentoService.listarFaqs()));
    }

    @PostMapping
    public ResponseEntity<BaseConhecimentoModel> cadastrarFaq(@RequestBody @Valid BaseConhecimentoCadastroInput baseConhecimentoCadastroInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(baseConhecimentoModelAssembler.toModel(
            baseConhecimentoService.criarFaq(baseConhecimentoCadastroInputDisassembler.toDomainObject(baseConhecimentoCadastroInput))
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseConhecimentoModel> editarFaq(@RequestBody BaseConhecimentoEdicaoInput baseConhecimentoEdicaoInput, @PathVariable Long idBase) {
        return ResponseEntity.status(HttpStatus.OK).body(baseConhecimentoModelAssembler.toModel(
           baseConhecimentoService.alterarFaq(idBase, baseConhecimentoEdicaoInputDisassembler.toDomainObject(baseConhecimentoEdicaoInput))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarFaq(@AuthenticationPrincipal Usuario usuario,@PathVariable Long idBase) {
        baseConhecimentoService.deletarFaq(usuario, idBase);

        return ResponseEntity.status(HttpStatus.OK).body("Deletado");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseConhecimentoModel> buscarPorId(@PathVariable Long idBase) {
        return ResponseEntity.status(HttpStatus.OK).body(baseConhecimentoModelAssembler.toModel(
            baseConhecimentoService.buscarFaqPorId(idBase) 
        ));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<BaseConhecimentoModel>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(baseConhecimentoModelAssembler.toCollectionModel(
            baseConhecimentoService.buscarPorNome(nome)
        ));
    }
}
