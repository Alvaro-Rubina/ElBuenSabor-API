package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InsumoDTO {

    private String denominacion;
    private String urlImagen;
    private Double precioCompra;
    private Double precioVenta;
    private Double stockActual;
    private Double stockMinimo;
    private boolean esParaElaborar;
    private boolean activo;
    private UnidadMedida unidadMedida;
    private RubroInsumoDTO rubro;
}
