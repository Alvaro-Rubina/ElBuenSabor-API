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
public class Insumo extends Base {

    private String denominacion;

    @Column(length = 500)
    private String urlImagen;

    private Double precioCosto;

    private Double precioVenta;

    private Double stockActual;

    private Double stockMinimo;

    private boolean esParaElaborar;

    private boolean activo;

    @Enumerated(EnumType.STRING)
    private UnidadMedida unidadMedida;

    @ManyToOne
    @JoinColumn(name = "rubroInsumo_id")
    private RubroInsumo rubro;
}
