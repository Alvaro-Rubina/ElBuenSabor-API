package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Usuario extends Base {

    @Column(unique = true)
    private String email;

    private String contrasenia;

    @Column(unique = true)
    private String auth0Id;

    @Enumerated(EnumType.STRING)
    private Rol rol;
}
