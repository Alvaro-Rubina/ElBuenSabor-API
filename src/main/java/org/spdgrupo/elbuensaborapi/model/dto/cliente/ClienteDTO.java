package org.spdgrupo.elbuensaborapi.model.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    
    private String telefono;

    private Boolean activo;

    @NotNull(message = "El campo usuario no puede ser nulo")
    private UsuarioDTO usuario;
}
