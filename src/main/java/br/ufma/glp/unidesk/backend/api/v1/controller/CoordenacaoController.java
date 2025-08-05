package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoCadastroInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoEdicaoInputDisassembler;
import br.ufma.glp.unidesk.backend.api.v1.assembler.CoordenacaoModelAssembler;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenacaoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.CoordenacaoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenacaoModel;
import br.ufma.glp.unidesk.backend.domain.model.Coordenacao;
import br.ufma.glp.unidesk.backend.domain.service.CoordenacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CoordenacaoModel adicionar(@RequestBody @Valid CoordenacaoCadastroInput coordenacaoInput) {
        return coordenacaoModelAssembler.toModel(
                coordenacaoService.salvar(coordenacaoCadastroInputDisassembler.toDomainObject(coordenacaoInput))
        );
    }

    @PreAuthorize( "hasRole('ADMIN') or hasRole('COORDENADOR') or hasRole('FUNCIONARIO_COORDENACAO')" )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CoordenacaoModel atualizar(@PathVariable Long id,@RequestBody @Valid CoordenacaoEdicaoInput coordenacaoEdicaoInput) {
        Coordenacao coordenacaoExistente = coordenacaoService.buscarPorIdOuFalhar(id);
        coordenacaoEdicaoInputDisassembler.copyToDomainObject(coordenacaoEdicaoInput, coordenacaoExistente);
        return coordenacaoModelAssembler.toModel(coordenacaoService.atualizar(coordenacaoExistente));
    }

    @PreAuthorize( "hasRole('ADMIN')" )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        coordenacaoService.excluir(id);
    }
}