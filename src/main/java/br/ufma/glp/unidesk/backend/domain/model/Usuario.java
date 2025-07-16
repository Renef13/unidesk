package br.ufma.glp.unidesk.backend.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "usuarios")
public abstract class Usuario {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false, unique = true)
    private Long idUsuario;

    @Column(name = "nome", nullable = false, length = 100)
    @NotBlank(message = "O nome do usuário não pode ser nulo ou vazio")
    private String nome;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    @NotBlank(message = "O email do usuário não pode ser nulo ou vazio")
    @Email(message = "O email deve ser válido")
    private String email;

    @Column(name = "senha", nullable = false, length = 100)
    @NotBlank(message = "A senha do usuário não pode ser nula ou vazia")
    private String senha;
}
