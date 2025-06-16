package org.spdgrupo.elbuensaborapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class Usuario extends Base {

    @Column(unique = true)
    private String email;

    private String nombreCompleto;

    @Column(unique = true)
    private String auth0Id;

    private String connection;

    private String nickName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("usuarios")
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
}
