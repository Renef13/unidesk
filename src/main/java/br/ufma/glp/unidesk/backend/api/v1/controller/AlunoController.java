package br.ufma.glp.unidesk.backend.api.v1.controller;

import br.ufma.glp.unidesk.backend.domain.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/alunos")
public class AlunoController {
    private final AlunoService alunoService;
}
