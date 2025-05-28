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

    private Boolean activo;

    private UsuarioDTO usuario;
}
