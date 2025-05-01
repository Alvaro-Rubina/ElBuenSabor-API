package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioDTO {
    private Long id;
    @NotBlank(message = "el campo email no puede estar vacio")
    private String email;
    @NotBlank(message = "el campo contraseña no puede estar vacio")
    private String contraseña;
    @NotBlank(message = "el campo auth0Id no puede estar vacio")
    private String auth0Id;
    private Rol rol;
}

