package org.spdgrupo.elbuensaborapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.entity.auth0.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Rol extends Base {

    private String nombre;
    private String descripcion;
    private String auth0RolId;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> usuarios = new HashSet<>();
}