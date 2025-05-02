package org.spdgrupo.elbuensaborapi.model.dto.usuario;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @NotBlank(message = "El campo email no puede estar vacio")
    private String email;

    @NotBlank(message = "El campo contraseña no puede estar vacio")
    private String contraseña;

    @NotNull(message = "el campo rol no puede estar vacio")
    @Enumerated(EnumType.STRING)
    private Rol rol;
}

