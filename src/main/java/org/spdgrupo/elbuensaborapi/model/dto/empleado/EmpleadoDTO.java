package org.spdgrupo.elbuensaborapi.model.dto.empleado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El campo usuario no puede ser nulo")
    private UsuarioDTO usuario;

    private DomicilioDTO domicilio;
}
