package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.CoordenacaoModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoModel {

    private Long idCurso;

    private String nome;

    private String campus;

    // Add coordinations list
    private List<CoordenacaoModel> coordenacoes;
}