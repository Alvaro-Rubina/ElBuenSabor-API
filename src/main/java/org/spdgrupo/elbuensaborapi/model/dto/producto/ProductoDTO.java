package org.spdgrupo.elbuensaborapi.model.dto.producto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductoDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;

    @NotBlank(message = "El campo descripcion no puede estar vacio")
    private String descripcion;

    @NotNull(message = "El campo tiempoEstimadoPreparacion no puede ser nulo")
    @Min(value = 1, message = "El campo tiempoEstimadoPreparacion no puede ser menor a 1")
    private Long tiempoEstimadoPreparacion;

    @NotNull(message = "El campo precioCosto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "el campo precioCosto no puede ser menor a 0.01")
    private Double precioVenta;

    @NotBlank(message = "El campo urlImagen no puede estar vacio")
    private String urlImagen;

    @NotNull(message = "El campo activo no puede ser nulo")
    private Boolean activo;

    @NotNull(message = "El campo rubroId no puede ser nulo")
    @Min(value = 1, message = "El campo rubroId no puede ser menor a 1")
    private Long rubroId;

    @Size(min = 1, message = "El campo detalleProductos debe contener por lo menos 1 elemento")
    private List<DetalleProductoDTO> detalleProductos;
}
