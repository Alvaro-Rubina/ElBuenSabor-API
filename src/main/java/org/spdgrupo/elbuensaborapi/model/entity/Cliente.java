package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCompleto;
    private String telefono;
    private Boolean activo;

    @OneToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "cliente")
    private List<DetalleDomicilio> detalleDomicilios;

}
