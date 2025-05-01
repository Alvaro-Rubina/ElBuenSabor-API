package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetalleFacturaDTO {

    private Long id;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int cantidad;

    @NotNull(message = "El subtotal no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El subtotal debe ser mayor a 0")
    private Double subTotal;

    private FacturaDTO factura;
    private ProductoDTO producto;
    private InsumoDTO insumo;
}
