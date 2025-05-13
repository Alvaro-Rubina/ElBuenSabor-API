package org.spdgrupo.elbuensaborapi.model.dto.cliente;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioPatchDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClientePatchDTO {

    private String nombreCompleto;
    private String telefono;
    private Boolean activo;
    private UsuarioPatchDTO usuario;
}