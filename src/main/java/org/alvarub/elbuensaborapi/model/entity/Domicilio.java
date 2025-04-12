package org.alvarub.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String calle;
    private String localidad;
    private Integer numero;
    private Integer codigoPostal;

    @OneToMany(mappedBy = "domicilio")
    private List<DetalleDomicilio> detalleDomicilios;


}
