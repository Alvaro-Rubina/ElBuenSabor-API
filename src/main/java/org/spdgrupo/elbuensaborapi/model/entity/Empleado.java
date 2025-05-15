package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Empleado extends Base {

    private String nombreCompleto;

    private String telefono;

    private Boolean activo;

    @OneToOne
    private Usuario usuario;

    @OneToOne
    private Domicilio domicilio;
}
