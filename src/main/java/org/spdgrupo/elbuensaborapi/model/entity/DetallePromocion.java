package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class DetallePromocion extends Base {

    private Integer cantidad;

    private Double subTotal;

    private Double subTotalCosto;

    @ManyToOne
    @JoinColumn(name = "promocion_id")
    private Promocion promocion;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;
}
