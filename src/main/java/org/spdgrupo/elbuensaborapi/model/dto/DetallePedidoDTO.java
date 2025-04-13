package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetallePedidoDTO {

    private Long id;
    private Integer cantidad;
    private Double subTotal;
    private PedidoDTO pedido;
    private ProductoDTO producto;
    private InsumoDTO insumo;
}
