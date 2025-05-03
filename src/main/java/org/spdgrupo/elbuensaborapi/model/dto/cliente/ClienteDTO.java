package org.spdgrupo.elbuensaborapi.model.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDTO {

    @NotBlank (message = "El campo nombreCompleto no puede estar vacío")
    private String nombreCompleto;

    @NotBlank (message = "El campo telefono no puede estar vacío")
    private String telefono;

    // este campo es opcional, util para EDITAR un cliente ya que al crearlo por defecto activo = true
    private boolean activo;

    private UsuarioDTO usuario;
}
