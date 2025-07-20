package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relatorio {
    //TODO: Adicionar validações de conteúdo, data e tipoRelatorio

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRelatorio;
    private String conteudo;
    private LocalDate data;
    private String tipoRelatorio;
}
