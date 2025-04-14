package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad;
    private Double subTotal;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;
}
