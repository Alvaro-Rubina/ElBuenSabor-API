package org.alvarub.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denominacion;
    private String descripcion;
    private Double precioVenta;
    private String urlImagen;
    private Boolean activo;

    @ManyToOne
    private RubroProducto rubro;

    @OneToMany(mappedBy = "producto")
    private List<DetalleProducto> detallesProducto;

    @OneToMany(mappedBy = "producto")
    private List<DetalleFactura> detallesFactura;

    @OneToMany(mappedBy = "producto")
    private List<DetallePromocion> detallesPromocion;
}
