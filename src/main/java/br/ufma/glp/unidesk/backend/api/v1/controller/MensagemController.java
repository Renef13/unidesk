package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.MensagemCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.MensagemEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.MensagemModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.MensagemCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.MensagemEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.MensagemModel;
import br.ufma.glp.unidesk.backend.domain.model.Mensagem;
import br.ufma.glp.unidesk.backend.domain.service.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/mensagens")
public class MensagemController {

    private final MensagemService mensagemService;
    private final MensagemModelAssembler mensagemModelAssembler;
    private final MensagemCadastroInputDisassembler mensagemCadastroInputDisassembler;
    private final MensagemEdicaoInputDisassembler mensagemEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<MensagemModel> listar() {
        return mensagemModelAssembler.toCollectionModel(mensagemService.listarTodas());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MensagemModel buscarPorId(@PathVariable Long id) {
        return mensagemModelAssembler.toModel(mensagemService.buscarPorIdOuFalhar(id));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public MensagemModel adicionar(@RequestBody @Valid MensagemCadastroInput mensagemInput) {
        return mensagemModelAssembler.toModel(
                mensagemService.salvar(mensagemCadastroInputDisassembler.toDomainObject(mensagemInput))
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MensagemModel atualizar(@PathVariable Long id, @RequestBody @Valid MensagemEdicaoInput mensagemEdicaoInput) {
        Mensagem mensagemExistente = mensagemService.buscarPorIdOuFalhar(id);
        mensagemEdicaoInputDisassembler.copyToDomainObject(mensagemEdicaoInput, mensagemExistente);
        return mensagemModelAssembler.toModel(mensagemService.salvar(mensagemExistente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        mensagemService.remover(id);
    }
}