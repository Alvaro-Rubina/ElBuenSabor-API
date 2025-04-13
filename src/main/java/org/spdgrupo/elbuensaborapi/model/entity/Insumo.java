package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
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
}
