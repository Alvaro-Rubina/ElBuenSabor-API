package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class DetalleFactura extends Base {

    private Integer cantidad;

    private Double subTotal;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

}
