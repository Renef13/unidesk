package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "prioridades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Prioridade {
    //TODO: analisar se não vale a pena usar enum
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prioridade", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long idPrioridade;

    @Column(name = "nivel", nullable = false, unique = true, length = 20)
    @NotBlank(message = "O nível de prioridade não pode ser vazio")
    @Size(max = 20, message = "O nível de prioridade deve ter no máximo 20 caracteres")
    private String nivel;
}
