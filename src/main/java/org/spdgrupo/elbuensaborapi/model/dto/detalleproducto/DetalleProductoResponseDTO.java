package org.spdgrupo.elbuensaborapi.model.dto.detalleproducto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetalleProductoResponseDTO {

    private Long id;
    private Double cantidad;
    private InsumoResponseDTO insumo;
}
