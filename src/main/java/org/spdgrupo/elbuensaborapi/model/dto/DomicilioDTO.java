package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DomicilioDTO {

    private Long id;
    @NotBlank(message = "El campo 'calle' no puede estar vacío")
    private String calle;
    @NotBlank(message = "El campo 'localidad' no puede estar vacío")
    private String localidad;
    @NotNull(message = "El campo 'numero' no puede estar vacío")
    private Integer numero;
    @NotNull(message = "El campo 'codigoPostal' no puede estar vacío")
    private Integer codigoPostal;
    @NotNull
    private Boolean activo;

}
