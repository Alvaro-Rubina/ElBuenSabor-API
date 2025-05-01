package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDTO {

    private Long id;

    @NotBlank (message = "El campo nombreCompleto no puede estar vacío")
    private String nombreCompleto;

    @NotBlank (message = "El campo telefono no puede estar vacío")
    private String telefono;

    private UsuarioDTO usuario;
    private Boolean activo;
}
