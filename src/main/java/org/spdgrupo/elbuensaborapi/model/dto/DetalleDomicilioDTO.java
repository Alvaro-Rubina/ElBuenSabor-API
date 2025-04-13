package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetalleDomicilioDTO {

    private Long id;
    private Long idCliente;
    private Long idDomicilio;

}
