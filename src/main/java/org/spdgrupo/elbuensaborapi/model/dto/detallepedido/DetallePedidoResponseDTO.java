package org.spdgrupo.elbuensaborapi.model.dto.detallepedido;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetallePedidoResponseDTO {

    private Long id;
    private Integer cantidad;
    private Double subTotal;
    private Double subTotalCosto;
    private ProductoResponseDTO producto;
    private InsumoResponseDTO insumo;
}
