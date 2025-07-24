package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CursoModel;
import br.ufma.glp.unidesk.backend.domain.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final CursoModelAssembler cursoModelAssembler;
    private final CursoCadastroInputDisassembler cursoCadastroInputDisassembler;
    private final CursoEdicaoInputDisassembler cursoEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<CursoModel> listar() {
        return cursoModelAssembler.toCollectionModel(cursoService.listarCursos());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CursoModel buscarPorId(@PathVariable Long id) {
        return cursoModelAssembler.toModel(cursoService.buscarCursoPorId(id));
    }

    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public List<CursoModel> buscarPorNome(@RequestParam String nome) {
        return cursoModelAssembler.toCollectionModel(cursoService.buscarCursoPorNome(nome));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CursoModel adicionar(@RequestBody @Valid CursoCadastroInput cursoCadastroInput) {
        return cursoModelAssembler.toModel(
                cursoService.criarCurso(cursoCadastroInputDisassembler.toDomainObject(cursoCadastroInput))
        );
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public CursoModel atualizar(@RequestBody @Valid CursoEdicaoInput cursoEdicaoInput) {
        return cursoModelAssembler.toModel(
                cursoService.alterarCurso(
                        cursoEdicaoInput.getIdCurso(),
                        cursoEdicaoInputDisassembler.toDomainObject(cursoEdicaoInput)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        cursoService.desativarCurso(id);
    }
}