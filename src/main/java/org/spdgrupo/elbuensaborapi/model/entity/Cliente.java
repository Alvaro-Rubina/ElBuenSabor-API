package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class Cliente extends Base {

    private String nombreCompleto;

    private String telefono;

    private Boolean activo;

    @OneToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "cliente")
    private List<DetalleDomicilio> detalleDomicilios;

}
