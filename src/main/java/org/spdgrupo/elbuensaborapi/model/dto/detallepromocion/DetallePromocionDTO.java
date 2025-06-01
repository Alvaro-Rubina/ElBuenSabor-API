package org.spdgrupo.elbuensaborapi.model.dto.detallepromocion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetallePromocionDTO {

    @NotNull(message = "El campo cantidad no puede ser nulo")
    @Min(value = 1, message = "El campo cantidad no puede ser menor a 1")
    private Integer cantidad;

    @Min(value = 1, message = "El campo productoId no puede ser menor a 1")
    private Long productoId;

    @Min(value = 1, message = "El campo insumoId no puede ser menor a 1")
    private Long insumoId;
}
