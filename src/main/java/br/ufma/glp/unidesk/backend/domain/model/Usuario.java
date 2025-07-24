package br.ufma.glp.unidesk.backend.domain.model;


import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public abstract class Usuario implements UserDetails {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false, unique = true)
    private Long idUsuario;

    @Column(name = "nome", nullable = false, length = 100)
    @NotBlank(message = "O nome do usuário não pode ser nulo ou vazio")
    private String nome;

    @NotBlank(message = "O nome de usuário não pode ser nula ou vazia, e deve ser único")
    @Column(nullable = false, unique = true)
    private String usuario;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    @NotBlank(message = "O email do usuário não pode ser nulo ou vazio")
    @Email(message = "O email deve ser válido")
    private String email;

    @Column(name = "senha", nullable = false, length = 100)
    @NotBlank(message = "A senha do usuário não pode ser nula ou vazia")
    private String senha;

    @Column(name = "role", nullable = false)
    @NotNull(message = "O papel do usuário é obrigatório")
    @Enumerated(EnumType.STRING)
    private UsuarioRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UsuarioRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
