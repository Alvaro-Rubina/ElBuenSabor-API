package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;
import org.alvarub.elbuensaborapi.model.Enums.Rol;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioDto {
    private Long id;
    private String nombreUsuario;
    private String contraseña;
    private String auth0Id;
    private Rol rol;
}

