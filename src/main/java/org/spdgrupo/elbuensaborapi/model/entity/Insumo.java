package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class Insumo extends Base {

    private String denominacion;

    private String descripcion;

    @Column(length = 500)
    private String urlImagen;

    private Double precioCosto;

    private Double precioVenta;

    private Double stockActual;

    private Double stockMinimo;

    private Boolean esParaElaborar;

    @Enumerated(EnumType.STRING)
    private UnidadMedida unidadMedida;

    @ManyToOne
    @JoinColumn(name = "rubroInsumo_id")
    private RubroInsumo rubro;
}
