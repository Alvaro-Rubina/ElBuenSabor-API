package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetalleDomicilioDTO {

    private Long id;
    private ClienteDTO cliente;
    private DomicilioDTO domicilio;

}
