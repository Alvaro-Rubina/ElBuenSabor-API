package org.spdgrupo.elbuensaborapi.model.dto;

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

    private Long id;
    @NotBlank(message = "el campo 'denominacion' no puede estar vacio")
    private String denominacion;
    @NotBlank(message = "el campo 'urlImagen' no puede estar vacio")
    private String urlImagen;
    @NotNull(message = "el campo 'fechaDesde' no puede estar vacío")
    @FutureOrPresent(message = "el campo 'fechaDesde' debe ser una fecha presente o futura")
    private LocalDate fechaDesde;
    @NotNull(message = "el campo 'fechaHasta' no puede estar vacío")
    @FutureOrPresent(message = "el campo 'fechaHasta' debe ser una fecha presente o futura")
    private LocalDate fechaHasta;
    @NotNull
    @DecimalMin(value = "0.0", message = "el campo 'descuento' no puede ser menor a 0")
    private Double descuento;
}
