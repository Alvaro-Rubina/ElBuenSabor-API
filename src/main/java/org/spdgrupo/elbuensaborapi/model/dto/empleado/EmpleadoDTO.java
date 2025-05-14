package org.spdgrupo.elbuensaborapi.model.dto.empleado;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmpleadoDTO {

    @NotBlank(message = "El campo nombreCompleto no puede estar vacío")
    private String nombreCompleto;

    @NotBlank(message = "El campo telefono no puede estar vacío")
    private String telefono;

    private UsuarioDTO usuario;

    private DomicilioDTO domicilio;
}
