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
    private String localidad;
    private Integer codigoPostal;
    private Boolean activo;
    private String infoAdicional;
}
