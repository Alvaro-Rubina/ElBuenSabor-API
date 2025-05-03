package org.spdgrupo.elbuensaborapi.model.dto.detallepedido;

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

    @NotNull(message = "El campo cantidad no puede ser nulo")
    @Min(value = 1, message = "La cantidad debe ser mayor a 1")
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0,1", message = "El subtotal debe ser mayor a 0.1")
    private Double subTotal;

    @NotNull(message = "El campo pedidoId no puede ser nulo")
    @Min(value = 1, message = "El campo pedidoId no puede ser menor a 1")
    private Long pedidoId;

    @NotNull(message = "El campo productoId no puede ser nulo")
    @Min(value = 1, message = "El campo productoId no puede ser menor a 1")
    private Long productoId;

    @NotNull(message = "El campo insumoId no puede ser nulo")
    @Min(value = 1, message = "El campo insumoId no puede ser menor a 1")
    private Long insumoId;
}
