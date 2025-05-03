package org.spdgrupo.elbuensaborapi.model.dto.promocion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromocionDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;

    @NotBlank(message = "El campo urlImagen no puede estar vacio")
    private String urlImagen;

    @NotNull(message = "El campo fechaDesde no puede ser nulo")
    @FutureOrPresent(message = "El campo fechaDesde debe ser una fecha presente o futura")
    private LocalDate fechaDesde;

    @NotNull(message = "El campo fechaHasta no puede ser nulo")
    @FutureOrPresent(message = "El campo fechaHasta debe ser una fecha presente o futura")
    private LocalDate fechaHasta;

    @NotNull(message = "El campo descuento no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El campo descuento no puede ser menor a 0")
    private Double descuento;
}
