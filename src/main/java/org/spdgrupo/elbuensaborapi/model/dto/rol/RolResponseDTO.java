package org.spdgrupo.elbuensaborapi.model.dto.rol;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RolResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String auth0RolId;
}
