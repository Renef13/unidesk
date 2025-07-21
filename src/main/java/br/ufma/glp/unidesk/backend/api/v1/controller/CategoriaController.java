package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CategoriaCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CategoriaEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CategoriaModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CategoriaCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CategoriaEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CategoriaModel;
import br.ufma.glp.unidesk.backend.domain.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler categoriaModelAssembler;
    private final CategoriaCadastroInputDisassembler categoriaCadastroInputDisassembler;
    private final CategoriaEdicaoInputDisassembler categoriaEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaModel> listar() {
        return categoriaModelAssembler.toCollectionModel(categoriaService.listarTodos());
    }

    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaModel> buscarPorNome(@RequestParam String nome) {
        return categoriaModelAssembler.toCollectionModel(categoriaService.buscarPorNomeContendo(nome));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoriaModel buscarPorId(@PathVariable Long id) {
        return categoriaModelAssembler.toModel(
                categoriaService.buscarPorIdOuFalhar(id));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaModel adicionar(@RequestBody @Valid CategoriaCadastroInput categoriaInput) {
        return categoriaModelAssembler.toModel(
                categoriaService
                        .salvar(categoriaCadastroInputDisassembler
                                .toDomainObject(categoriaInput)));
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public CategoriaModel atualizar(@RequestBody @Valid CategoriaEdicaoInput categoriaEdicaoInput) {
        return categoriaModelAssembler.toModel(
                categoriaService
                        .atualizar(categoriaEdicaoInputDisassembler
                                .toDomainObject(categoriaEdicaoInput)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        categoriaService.excluir(id);
    }

}
