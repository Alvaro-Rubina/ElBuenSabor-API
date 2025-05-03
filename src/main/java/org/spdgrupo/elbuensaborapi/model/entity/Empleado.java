package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCompleto;
    private String telefono;
    private boolean activo;

    @OneToOne
    private Usuario usuario;

    @OneToOne
    private Domicilio domicilio;
}
