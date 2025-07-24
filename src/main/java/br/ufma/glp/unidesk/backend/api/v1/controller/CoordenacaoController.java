package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenacaoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenacaoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenacaoModel;
import br.ufma.glp.unidesk.backend.domain.service.CoordenacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coordenacoes")
public class CoordenacaoController {

    private final CoordenacaoService coordenacaoService;
    private final CoordenacaoModelAssembler coordenacaoModelAssembler;
    private final CoordenacaoCadastroInputDisassembler coordenacaoCadastroInputDisassembler;
    private final CoordenacaoEdicaoInputDisassembler coordenacaoEdicaoInputDisassembler;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<CoordenacaoModel> listar() {
        return coordenacaoModelAssembler.toCollectionModel(coordenacaoService.listarTodas());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CoordenacaoModel buscarPorId(@PathVariable Long id) {
        return coordenacaoModelAssembler.toModel(coordenacaoService.buscarPorIdOuFalhar(id));
    }

    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public List<CoordenacaoModel> buscarPorNome(@RequestParam String nome) {
        return coordenacaoModelAssembler.toCollectionModel(coordenacaoService.buscarPorNome(nome));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CoordenacaoModel adicionar(@RequestBody @Valid CoordenacaoCadastroInput coordenacaoInput) {
        return coordenacaoModelAssembler.toModel(
                coordenacaoService.salvar(coordenacaoCadastroInputDisassembler.toDomainObject(coordenacaoInput))
        );
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public CoordenacaoModel atualizar(@RequestBody @Valid CoordenacaoEdicaoInput coordenacaoEdicaoInput) {
        return coordenacaoModelAssembler.toModel(
                coordenacaoService.atualizar(
                        coordenacaoEdicaoInput.getIdCoordenacao(),
                        coordenacaoEdicaoInputDisassembler.toDomainObject(coordenacaoEdicaoInput)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        coordenacaoService.excluir(id);
    }
}