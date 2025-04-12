package org.alvarub.elbuensaborapi.model.dto;

import lombok.*;
import org.alvarub.elbuensaborapi.model.entity.DetalleDomicilio;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DomicilioDto {

    private Long id;
    private String calle;
    private String localidad;
    private Integer numero;
    private Integer codigoPostal;
    private List<DetalleDomicilioDto> detalleDomicilios;

}
