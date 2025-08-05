package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.AlunoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.AlunoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.AlunoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.AlunoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.AlunoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.AlunoModel;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final AlunoModelAssembler alunoModelAssembler;
    private final AlunoCadastroInputDisassembler alunoCadastroInputDisassembler;
    private final AlunoEdicaoInputDisassembler alunoEdicaoInputDisassembler;

    @PreAuthorize( "hasRole('ADMIN')")
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<AlunoModel> listar() {
        return alunoModelAssembler.toCollectionModel(alunoService.listarTodos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlunoModel buscarPorId(@PathVariable Long id) {
        return alunoModelAssembler.toModel(alunoService.buscarPorIdOuFalhar(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public AlunoModel buscarPorNome(@RequestParam String nome) {
        return alunoModelAssembler.toModel(alunoService.buscarPorNomeOuFalhar(nome));
    }

@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoModel adicionar(@RequestBody @Valid AlunoCadastroInput alunoInput) {
        return alunoModelAssembler.toModel(
                alunoService.salvar(alunoCadastroInputDisassembler.toDomainObject(alunoInput))
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlunoModel atualizar(@PathVariable Long id, @RequestBody @Valid AlunoEdicaoInput alunoEdicaoInput) {

        Aluno alunoAtual = alunoService.buscarPorIdOuFalhar(id);

        alunoEdicaoInputDisassembler.copyToDomainObject(alunoEdicaoInput, alunoService.buscarPorIdOuFalhar(id));

        Aluno alunoAtualizado = alunoService.atualizar(alunoAtual);

        return alunoModelAssembler.toModel(alunoAtualizado);
    }

    @PreAuthorize( "hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        alunoService.excluir(id);
    }
}