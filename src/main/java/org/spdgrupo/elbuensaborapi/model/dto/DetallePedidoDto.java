package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

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
