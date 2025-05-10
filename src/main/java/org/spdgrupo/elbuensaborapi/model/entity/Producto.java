package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denominacion;
    private String descripcion;
    private Long tiempoEstimadoPreparacion;
    private Double precioVenta;
    private Double precioCosto;
    private String urlImagen;
    private boolean activo;

    @ManyToOne
    private RubroProducto rubro;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleProducto> detalleProductos;
}
