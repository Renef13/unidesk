package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrioridadeModel {

    private Long idPrioridade;

    private String nivel;
}