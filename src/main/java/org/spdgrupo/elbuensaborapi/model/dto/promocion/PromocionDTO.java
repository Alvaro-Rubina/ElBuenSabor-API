package org.spdgrupo.elbuensaborapi.model.dto.promocion;

import jakarta.validation.constraints.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromocionDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;

    /*@NotBlank(message = "El campo descripcion no puede estar vacio")*/
    private String descripcion = " ";

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

    @Size(min = 1, message = "El campo detallePromociones debe contener por lo menos 1 elemento")
    private List<DetallePromocionDTO> detallePromociones = new ArrayList<>();

    private Boolean activo = false;
}
