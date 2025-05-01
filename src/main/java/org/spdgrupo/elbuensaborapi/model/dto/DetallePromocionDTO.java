package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetallePromocionDTO {

    private Long id;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int cantidad;

    private PromocionDTO promocion;
    private ProductoDTO producto;
    private InsumoDTO insumo;
}
