package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private UsuarioDTO usuario;
}
