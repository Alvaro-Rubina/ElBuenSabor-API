package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class Empleado extends Base {

    private String nombreCompleto;

    private String telefono;

    @OneToOne
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    private Domicilio domicilio;
}
