package com.artuhin.sproject.model.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@Entity
@Table(schema = "bs", name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString()
public class RoleEntity implements GrantedAuthority {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @Transient
    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            schema = "bs",
            name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> users;

    @Override
    public String getAuthority() {
        return getName();
    }
}
