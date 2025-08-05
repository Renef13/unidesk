package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.FuncionarioCoordenacaoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.FuncionarioCoordenacaoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.FuncionarioCoordenacaoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.FuncionarioCoordenacaoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.FuncionarioCoordenacaoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.FuncionarioCoordenacaoModel;
import br.ufma.glp.unidesk.backend.domain.model.FuncionarioCoordenacao;
import br.ufma.glp.unidesk.backend.domain.service.AuthService;
import br.ufma.glp.unidesk.backend.domain.service.FuncionarioCoordenacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import br.ufma.glp.unidesk.backend.domain.model.Usuario;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/funcionarios-coordenacao")
public class FuncionarioCoordenacaoController {

    private final FuncionarioCoordenacaoService funcionarioCoordenacaoService;
    private final FuncionarioCoordenacaoModelAssembler funcionarioCoordenacaoModelAssembler;
    private final FuncionarioCoordenacaoCadastroInputDisassembler funcionarioCoordenacaoCadastroInputDisassembler;
    private final FuncionarioCoordenacaoEdicaoInputDisassembler funcionarioCoordenacaoEdicaoInputDisassembler;
    private final AuthService authService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<FuncionarioCoordenacaoModel> listar() {
        Usuario usuario = authService.getCurrentUsuarioEntity();
        return funcionarioCoordenacaoModelAssembler.toCollectionModel(
                funcionarioCoordenacaoService.listarTodos(usuario)
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FuncionarioCoordenacaoModel buscarPorId(@PathVariable Long id) {
        return funcionarioCoordenacaoModelAssembler.toModel(
                funcionarioCoordenacaoService.buscarPorIdOuFalhar(id)
        );
    }

    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public List<FuncionarioCoordenacaoModel> buscarPorNome(@RequestParam String nome) {
        return funcionarioCoordenacaoModelAssembler.toCollectionModel(
                funcionarioCoordenacaoService.buscarPorNome(nome)
        );
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public FuncionarioCoordenacaoModel adicionar(@RequestBody @Valid FuncionarioCoordenacaoCadastroInput input) {
        var funcionario = funcionarioCoordenacaoCadastroInputDisassembler.toDomainObject(input);
        return funcionarioCoordenacaoModelAssembler.toModel(
                funcionarioCoordenacaoService.salvar(funcionario)
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FuncionarioCoordenacaoModel atualizar(@PathVariable Long id, @RequestBody @Valid FuncionarioCoordenacaoEdicaoInput input) {
        FuncionarioCoordenacao funcionarioExistente = funcionarioCoordenacaoService.buscarPorIdOuFalhar(id);
        funcionarioCoordenacaoEdicaoInputDisassembler.copyToDomainObject(input, funcionarioExistente);
        return funcionarioCoordenacaoModelAssembler.toModel(funcionarioCoordenacaoService.atualizar(funcionarioExistente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        funcionarioCoordenacaoService.remover(id);
    }
}