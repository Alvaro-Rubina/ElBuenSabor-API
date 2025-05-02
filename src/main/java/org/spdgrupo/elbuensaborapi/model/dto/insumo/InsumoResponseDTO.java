package org.spdgrupo.elbuensaborapi.model.dto.insumo;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InsumoResponseDTO {

    private Long id;
    private String denominacion;
    private String urlImagen;
    private Double precioCompra;
    private Double precioVenta;
    private Double stockActual;
    private Double stockMinimo;
    private boolean esParaElaborar;
    private boolean activo;
    private String unidadMedida;
    private RubroInsumoResponseDTO rubro;
}
