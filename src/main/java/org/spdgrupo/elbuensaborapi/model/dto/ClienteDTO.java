package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDTO {

    private Long id;
    private String nombreCompleto;
    private String telefono;
    private UsuarioDTO usuario;
    private Boolean activo;
}
