package org.alvarub.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.alvarub.elbuensaborapi.model.enums.UnidadMedida;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denominacion;
    private String urlImagen;
    private Double precioCompra;
    private Double precioVenta;
    private Double stockActual;
    private Double stockMinimo;
    private Boolean esParaElaborar;
    private Boolean activo;
    private UnidadMedida unidadMedida;


    @ManyToOne
    @JoinColumn(name = "rubroInsumo_id")
    private RubroInsumo rubro;

    @OneToMany(mappedBy = "insumo")
    private List<DetalleFactura> detallesFactura;

    @OneToMany(mappedBy = "insumo")
    private List<DetalleProducto> detallesProducto;

    @OneToMany(mappedBy = "insumo")
    private List<DetallePromocion> detallesPromocion;

}
