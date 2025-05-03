package org.spdgrupo.elbuensaborapi.model.dto.empleado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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

    // este campo es opcional, util para EDITAR un empleado ya que al crearlo por defecto activo = true
    private boolean activo;

    private UsuarioDTO usuario;

    @NotNull(message = "El campo domicilioId no puede ser nulo")
    private Long domicilioId;
}
