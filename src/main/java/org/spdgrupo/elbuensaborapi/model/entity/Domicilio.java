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
public class Domicilio extends Base {

    private String calle;

    private Integer numero;

    private String localidad;

    private Integer codigoPostal;

    private Double latitud;

    private Double longitud;


}
