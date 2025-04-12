package org.alvarub.elbuensaborapi.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetalleDomicilioDto {

    private Long id;
    private Long idCliente;
    private Long idDomicilio;

}
