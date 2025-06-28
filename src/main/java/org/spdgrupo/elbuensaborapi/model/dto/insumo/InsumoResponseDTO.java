package org.spdgrupo.elbuensaborapi.model.dto.insumo;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InsumoResponseDTO {

    private Long id;
    private String denominacion;
    private String descripcion;
    private String urlImagen;
    private Double precioCosto;
    private Double precioVenta;
    private Double stockActual;
    private Double stockMinimo;
    private boolean esParaElaborar;
    private boolean activo;
    private UnidadMedida unidadMedida;
    private RubroInsumoResponseDTO rubro;
}
