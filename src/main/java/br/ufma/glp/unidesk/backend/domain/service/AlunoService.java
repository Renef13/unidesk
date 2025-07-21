package br.ufma.glp.unidesk.backend.domain.service;

import br.ufma.glp.unidesk.backend.domain.repository.AlunoRepository;
import br.ufma.glp.unidesk.backend.domain.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

}
