package org.spdgrupo.elbuensaborapi.model.dto.domicilio;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DomicilioResponseDTO {

    private Long id;
    private String calle;
    private Integer numero;
    private String localidad;
    private Integer codigoPostal;
    private Boolean activo;
}
