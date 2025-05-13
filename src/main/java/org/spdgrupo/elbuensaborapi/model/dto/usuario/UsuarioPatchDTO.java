package org.spdgrupo.elbuensaborapi.model.dto.usuario;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioPatchDTO {

    private String contraseña;
    private Rol rol;
}