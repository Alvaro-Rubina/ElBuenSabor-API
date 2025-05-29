package org.spdgrupo.elbuensaborapi.model.dto.insumo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;

    @NotBlank(message = "El campo urlImagen no puede estar vacio")
    private String urlImagen;

    @NotNull(message = "El campo precioCompra no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El campo precioCompra no puede ser menor a 0")
    private Double precioCosto;

    private Double precioVenta;

    @NotNull(message = "El campo stockActual no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El campo stockActual no puede ser menor a 0")
    private Double stockActual;

    @NotNull(message = "El campo stockMinimo no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El campo stockMinimo no puede ser menor a 0")
    private Double stockMinimo;

    @NotNull(message = "El campo esParaElaborar no puede ser nulo")
    private Boolean esParaElaborar;

    private Boolean activo;

    @NotNull(message = "El campo unidadMedida no puede ser nulo")
    private UnidadMedida unidadMedida;

    @NotNull(message = "El campo rubro no puede ser nulo")
    @Min(value = 1, message = "El campo rubroId no puede ser menor a 1")
    private Long rubroId;
}
