package org.spdgrupo.elbuensaborapi.model.dto.detallepedido;

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

    // Estos campos son opcionales por separado, PERO siempre debe haber por lo menos 1
    private Long productoId;
    private Long insumoId;
}
