package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenadorCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenadorEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenadorModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenadorCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenadorEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenadorModel;
import br.ufma.glp.unidesk.backend.domain.model.Coordenador;
import br.ufma.glp.unidesk.backend.domain.service.CoordenadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coordenadores")
public class CoordenadorController {

    private final CoordenadorService coordenadorService;
    private final CoordenadorModelAssembler coordenadorModelAssembler;
    private final CoordenadorCadastroInputDisassembler coordenadorCadastroInputDisassembler;
    private final CoordenadorEdicaoInputDisassembler coordenadorEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<CoordenadorModel> listar() {
        return coordenadorModelAssembler.toCollectionModel(coordenadorService.listarTodos());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CoordenadorModel buscarPorId(@PathVariable Long id) {
        return coordenadorModelAssembler.toModel(coordenadorService.buscarPorIdOuFalhar(id));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CoordenadorModel adicionar(@RequestBody @Valid CoordenadorCadastroInput input) {
        return coordenadorModelAssembler.toModel(
                coordenadorService.salvar(coordenadorCadastroInputDisassembler.toDomainObject(input))
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CoordenadorModel atualizar(@PathVariable Long id, @RequestBody @Valid CoordenadorEdicaoInput coordenadorInput) {
        Coordenador coordenador = coordenadorService.buscarPorIdOuFalhar(id);
        coordenadorEdicaoInputDisassembler.copyToDomainObject(coordenadorInput, coordenador);
        return coordenadorModelAssembler.toModel(coordenadorService.atualizar(coordenador));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        coordenadorService.excluir(id);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public CoordenadorModel ativar(@PathVariable Long id) {
        return coordenadorModelAssembler.toModel(coordenadorService.ativar(id));
    }

    @PutMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.OK)
    public CoordenadorModel desativar(@PathVariable Long id) {
        return coordenadorModelAssembler.toModel(coordenadorService.desativar(id));
    }
}