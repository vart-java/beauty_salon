package com.artuhin.sproject.model.entity;

import com.artuhin.sproject.model.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

@Data
@Builder
@Entity
@Table(schema = "bs", name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "skills")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, message = "at least 5 characters")
    private String login;
    @Size(min = 2, message = "at least 5 characters")
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private double rating;
    private int recallCount;
    @ManyToMany
    @JoinTable(
            schema = "bs",
            name = "users_procedures",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "procedure_id")
    )
    private Set<ProcedureEntity> skills;
    @Transient
    private String passwordConfirm;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    @Override
    public String getUsername() {
        return login.substring(0, login.indexOf('@'));
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
