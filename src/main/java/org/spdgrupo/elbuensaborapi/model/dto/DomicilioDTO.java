package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DomicilioDTO {

    private Long id;
    private String calle;
    private String localidad;
    private Integer numero;
    private Integer codigoPostal;

}
