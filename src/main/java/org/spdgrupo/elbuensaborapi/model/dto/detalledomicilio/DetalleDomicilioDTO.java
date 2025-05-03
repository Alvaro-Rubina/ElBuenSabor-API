package org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DetalleDomicilioDTO {

    @NotNull(message = "El campo clienteId no puede ser nulo")
    @Min(value = 1, message = "El campo clienteId no puede ser menor a 1")
    private Long clienteId;

    private DomicilioDTO domicilio;

}
