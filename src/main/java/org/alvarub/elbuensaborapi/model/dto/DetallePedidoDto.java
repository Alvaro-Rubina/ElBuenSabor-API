package org.alvarub.elbuensaborapi.model.dto;

import lombok.*;
import org.alvarub.elbuensaborapi.model.entity.Pedido;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetallePedidoDto {

    private Long id;
    private Integer cantidad;
    private Double subTotal;
    private Long idPedido;
    //private Long idProducto;
}
