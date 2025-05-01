package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetallePedidoDTO {

    private Long id;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0,1", message = "El subtotal debe ser mayor a 0")
    private Double subTotal;

    private PedidoDTO pedido;
    private ProductoDTO producto;
    private InsumoDTO insumo;
}
