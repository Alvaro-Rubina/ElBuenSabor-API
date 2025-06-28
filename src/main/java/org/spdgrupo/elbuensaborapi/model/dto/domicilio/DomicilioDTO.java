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
    @NotNull(message = "El campo latitud no puede ser nulo")
    private Double latitud;

    @NotNull(message = "El campo longitud no puede ser nulo")
    private Double longitud;


}
