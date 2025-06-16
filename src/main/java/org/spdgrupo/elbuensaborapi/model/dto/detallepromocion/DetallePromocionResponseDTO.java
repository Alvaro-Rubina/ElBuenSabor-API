package org.spdgrupo.elbuensaborapi.model.dto.detallepromocion;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetallePromocionResponseDTO {

    private Long id;
    private Integer cantidad;
    private Double subTotal;
    private Double subTotalCosto;
    private ProductoResponseDTO producto;
    private InsumoResponseDTO insumo;
}
