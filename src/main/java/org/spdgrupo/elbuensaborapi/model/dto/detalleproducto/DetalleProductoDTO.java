package org.spdgrupo.elbuensaborapi.model.dto.detalleproducto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetalleProductoDTO {

    @NotNull(message = "El campo cantidad no puede ser nulo")
    @Min(value = 1, message = "El campo cantidad no puede ser menor a 1")
    private Double cantidad;

    @NotNull(message = "El campo productoId no puede ser nulo")
    @Min(value = 1, message = "El campo productoId no puede ser menor a 1")
    private Long productoId;

    @NotNull(message = "El campo insumoId no puede ser nulo")
    @Min(value = 1, message = "El campo insumoId no puede ser menor a 1")
    private Long insumoId;
}
