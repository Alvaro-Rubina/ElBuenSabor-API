package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductoDTO {

    private Long id;
    @NotBlank(message = "el campo denominacion no puede estar vacio")
    private String denominacion;
    @NotBlank(message = "el campo descripcion no puede estar vacio")
    private String descripcion;
    @NotNull(message = "el campo tiempoEstimadoPreparacion no puede estar vacio")
    @Min(value = 1, message = "el campo tiempoEstimadoPreparacion debe ser mayor a 0")
    private Long tiempoEstimadoPreparacion;
    @NotNull(message = "el campo precioCosto no puede estar vacio")
    @DecimalMin(value = "0.01", message = "el campo precioCosto debe ser mayor a 0")
    private Double precioVenta;
    @NotBlank(message = "el campo urlImagen no puede estar vacio")
    private String urlImagen;
    @NotNull(message = "el campo activo no puede estar vacio")
    private boolean activo;
    private RubroProductoDTO rubro;
}
