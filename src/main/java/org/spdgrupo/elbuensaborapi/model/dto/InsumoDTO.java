package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InsumoDTO {

    private Long id;
    @NotBlank(message = "el campo denominacion no puede estar vacio")
    private String denominacion;
    @NotBlank(message = "el campo urlImagen no puede estar vacio")
    private String urlImagen;
    @NotNull(message = "el campo precioCompra no puede estar vacio")
    @DecimalMin(value = "0.0", message = "el campo precioCompra no puede ser menor a 0")
    private Double precioCompra;
    @NotNull(message = "el campo precioVenta no puede estar vacio")
    @DecimalMin(value = "0.0", message = "el campo precioVenta no puede ser menor a 0")
    private Double precioVenta;
    @NotNull(message = "el campo stockActual no puede estar vacio")
    @DecimalMin(value = "0.0", message = "el campo stockActual no puede ser menor a 0")
    private Double stockActual;
    @NotNull(message = "el campo stockMinimo no puede estar vacio")
    @DecimalMin(value = "0.0", message = "el campo stockMinimo no puede ser menor a 0")
    private Double stockMinimo;
    @NotNull
    private boolean esParaElaborar;
    private boolean activo;
    private UnidadMedida unidadMedida;
    private RubroInsumoDTO rubro;
}
