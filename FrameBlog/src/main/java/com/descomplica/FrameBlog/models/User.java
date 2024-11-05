package com.descomplica.FrameBlog.models;

import com.descomplica.FrameBlog.deserializers.CustomAuthorityDeserializer;
import com.descomplica.FrameBlog.enums.RoleEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "User")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String name;
    private String email;
    private String username;
    private String password;
    private RoleEnum role;

    public User() {
    }
    public User(final Long userId, final String name, final String email, final String username, final String password, final RoleEnum role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters e Setters:
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }


    // Implementação dos métodos da interface UserDetails:
    // (Explicação dos métodos em: https://chatgpt.com/share/6729c382-838c-8011-a32d-ba4e97875c4e)
    @Override
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {  // From UserDetails interface
        if (this.role == RoleEnum.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),  // Obs: Granted Authority = Autoridade Concedida
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }
    // A anotação @JsonDeserialize instrui o Jackson (uma biblioteca de mapeamento JSON usada pelo Spring Boot)
    // a usar uma classe específica (CustomAuthorityDeserializer) para transformar os dados JSON que representam
    // as permissões do usuário em objetos Java.

    @Override
    public String getPassword() {
        return this.password;
    }   // From UserDetails interface
    // retorna a senha do usuário, que é usada pelo Spring Security para a autenticação.

    @Override
    public String getUsername() {
        return this.username;
    }   // From UserDetails interface
    // Retorna o nome de usuário (ou login) do usuário, que é necessário para autenticação.

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }   // From UserDetails interface
    // Indica se a conta do usuário está expirada.
    // Retornando true, indica que a conta nunca expira.

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }    // From UserDetails interface
    //  Indica se a conta do usuário está bloqueada.
    //  Retornando true, indica que a conta nunca é bloqueada.

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }   // From UserDetails interface
    // Indica se as credenciais (senha) do usuário estão expiradas.
    // Retornando true, indica que as credenciais nunca expiram.

    @Override
    public boolean isEnabled() {
        return true;
    }   // From UserDetails interface
    // Indica se o usuário está habilitado para autenticação.
    // Retornando true, indica que o usuário sempre está habilitado.

    // Em outras palavras, quando isEnabled() retorna true, ele sinaliza ao Spring Security que
    // essa conta de usuário está ativa e apta a acessar o sistema.
    // Se esse isEnabled() retornar false, o Spring Security vai bloquear automaticamente a
    // autenticação desse usuário, independentemente de ele ter fornecido credenciais válidas.
}