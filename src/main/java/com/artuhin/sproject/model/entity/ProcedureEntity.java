package com.artuhin.sproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.time.Duration;
import java.util.Set;

@Builder
@Data
@Entity
@Table(schema = "bs", name = "procedures")
@TypeDef(
        typeClass = PostgreSQLIntervalType.class,
        defaultForType = Duration.class
)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProcedureEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Duration duration;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            schema = "bs",
            name = "users_procedures",
            joinColumns = @JoinColumn(name = "procedure_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> masters;
}
