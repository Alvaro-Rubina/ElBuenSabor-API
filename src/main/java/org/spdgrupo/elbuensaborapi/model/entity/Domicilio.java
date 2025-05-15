package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Domicilio extends Base {

    private String calle;

    private Integer numero;

    private String localidad;

    private Integer codigoPostal;

    private Boolean activo;

}
