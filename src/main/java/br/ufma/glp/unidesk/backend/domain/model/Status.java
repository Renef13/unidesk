package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {
    //TODO: analisar se não vale a pena usar enum
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status", updatable = false, nullable = false)
    private Long idStatus;

    @Column(name = "nome", nullable = false, unique = true, length = 50)
    @NotBlank(message = "O nome do status não pode ser vazio")
    @Size(max = 50, message = "O nome do status deve ter no máximo 50 caracteres")
    private String nome;
}
