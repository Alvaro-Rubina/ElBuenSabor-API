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
public class Producto extends Base {

    private String denominacion;

    @Column(length = 1500)
    private String descripcion;

    private Long tiempoEstimadoPreparacion;

    private Double precioVenta;

    private Double precioCosto;

    @Column(length = 500)
    private String urlImagen;

    private Boolean activo;

    @ManyToOne
    private RubroProducto rubro;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleProducto> detalleProductos;
}
