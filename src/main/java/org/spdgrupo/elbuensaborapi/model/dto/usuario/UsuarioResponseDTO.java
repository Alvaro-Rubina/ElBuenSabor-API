package org.spdgrupo.elbuensaborapi.model.dto.usuario;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String email;
    private String auth0Id;
    private Rol rol;
}
