// src/main/java/org/spdgrupo/elbuensaborapi/model/entity/PedidoCodigoSequence.java
package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PedidoCodigoSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String anioMes; // Ejemplo: "2406"
    private int secuencia;
}