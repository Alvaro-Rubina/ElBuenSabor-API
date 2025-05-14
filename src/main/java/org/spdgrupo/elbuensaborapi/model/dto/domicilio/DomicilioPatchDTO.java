package org.spdgrupo.elbuensaborapi.model.dto.domicilio;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DomicilioPatchDTO {

    private String calle;
    private Integer numero;
    private String localidad;
    private Integer codigoPostal;
    private Boolean activo;
}
