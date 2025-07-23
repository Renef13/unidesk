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

import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CursoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CursoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CursoModel;
import br.ufma.glp.unidesk.backend.domain.model.Usuario;
import br.ufma.glp.unidesk.backend.domain.service.CursoService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/cursos")
public class CursoController {
    
    private CursoService cursoService;
    private CursoModelAssembler cursoModelAssembler;
    private CursoCadastroInputDisassembler cursoCadastroInputDisassembler;
    private CursoEdicaoInputDisassembler cursoEdicaoInputDisassembler;
    public CursoController(CursoService cursoService, CursoModelAssembler cursoModelAssembler,
            CursoCadastroInputDisassembler cursoCadastroInputDisassembler,
            CursoEdicaoInputDisassembler cursoEdicaoInputDisassembler) {
        this.cursoService = cursoService;
        this.cursoModelAssembler = cursoModelAssembler;
        this.cursoCadastroInputDisassembler = cursoCadastroInputDisassembler;
        this.cursoEdicaoInputDisassembler = cursoEdicaoInputDisassembler;
    }

    public ResponseEntity<List<CursoModel>> listarCursos() {
        return ResponseEntity.ok().body(cursoModelAssembler.toCollectionModel(cursoService.listarCursos()));
    }

     @PostMapping
    public ResponseEntity<CursoModel> cadastrarCurso(@RequestBody @Valid CursoCadastroInput cursoCadastroInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoModelAssembler.toModel(
            cursoService.criarCurso(cursoCadastroInputDisassembler.toDomainObject(cursoCadastroInput))
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoModel> editarCurso(@RequestBody CursoEdicaoInput cursoEdicaoInput, @PathVariable Long idCurso) {
        return ResponseEntity.status(HttpStatus.OK).body(cursoModelAssembler.toModel(
           cursoService.alterarCurso(idCurso, cursoEdicaoInputDisassembler.toDomainObject(cursoEdicaoInput))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCurso(@AuthenticationPrincipal Usuario usuario,@PathVariable Long idCurso) {
        cursoService.desativarCurso(usuario, idCurso);

        return ResponseEntity.status(HttpStatus.OK).body("Deletado");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoModel> buscarPorId(@PathVariable Long idCurso) {
        return ResponseEntity.status(HttpStatus.OK).body(cursoModelAssembler.toModel(
            cursoService.buscarCursoPorId(idCurso) 
        ));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CursoModel>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(cursoModelAssembler.toCollectionModel(
            cursoService.buscarCursoPorNome(nome)
        ));
    }

}
