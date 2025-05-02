package org.spdgrupo.elbuensaborapi.model.dto.domicilio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DomicilioDTO {

    @NotBlank(message = "El campo calle no puede estar vacío")
    private String calle;

    @NotNull(message = "El campo numero no puede ser nulo")
    private Integer numero;

    @NotBlank(message = "El campo localidad no puede estar vacío")
    private String localidad;

    @NotNull(message = "El campo codigoPostal no puede ser nulo")
    private Integer codigoPostal;

    // este campo es opcional
    private String infoAdicional;

}
