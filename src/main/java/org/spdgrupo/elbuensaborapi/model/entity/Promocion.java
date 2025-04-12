package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denominacion;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Double descuento;

    @OneToMany(mappedBy = "promocion")
    private List<DetallePromocion> detallesPromocion;
}
