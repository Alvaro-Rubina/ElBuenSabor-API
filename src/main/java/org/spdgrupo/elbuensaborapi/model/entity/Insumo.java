package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;
import org.spdgrupo.elbuensaborapi.model.interfaces.Vendible;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Insumo implements Vendible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denominacion;
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
