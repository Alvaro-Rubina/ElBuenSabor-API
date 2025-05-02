package org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetalleDomicilioResponseDTO {

    private Long id;
    private DomicilioResponseDTO domicilio;
}
