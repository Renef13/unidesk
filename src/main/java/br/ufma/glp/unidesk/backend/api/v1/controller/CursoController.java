package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CursoModel;
import br.ufma.glp.unidesk.backend.domain.model.Curso;
import br.ufma.glp.unidesk.backend.domain.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CursoModel adicionar(@RequestBody @Valid CursoCadastroInput cursoCadastroInput) {
        return cursoModelAssembler.toModel(
                cursoService.criarCurso(cursoCadastroInputDisassembler.toDomainObject(cursoCadastroInput))
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDENADOR') or hasRole('FUNCIONARIO_COORDENACAO')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CursoModel atualizar(@PathVariable Long id, @RequestBody @Valid CursoEdicaoInput cursoEdicaoInput) {
        Curso curso = cursoService.buscarCursoPorId(id);
        cursoEdicaoInputDisassembler.copyToDomainObject(cursoEdicaoInput, curso);
        return cursoModelAssembler.toModel(cursoService.atualizarCurso(curso));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        cursoService.desativarCurso(id);
    }
}