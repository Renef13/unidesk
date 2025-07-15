package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "coordenadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Coordenador extends Usuario {

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "matricula",nullable = false, unique = true)
    @NotBlank(message = "Matrícula não pode ser vazia ou conter apenas espaços em branco")
    @Size(max = 20, message = "Matrícula não pode ter mais de 20 caracteres")
    private String matricula;

    @ManyToOne
    @NotNull(message = "Coordenacao não pode ser nula")
    @JoinColumn(name = "id_coordenacao", nullable = false)
    private Coordenacao coordenacao;
}
