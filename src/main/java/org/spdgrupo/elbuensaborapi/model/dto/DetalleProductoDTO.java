package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetalleProductoDTO {

    private Long id;
    private Double cantidad;
    private UnidadMedida unidadMedida;
    private ProductoDTO producto;
    private InsumoDTO insumo;
}
