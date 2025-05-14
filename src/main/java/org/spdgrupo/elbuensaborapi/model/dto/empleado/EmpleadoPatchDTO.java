package org.spdgrupo.elbuensaborapi.model.dto.empleado;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioPatchDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmpleadoPatchDTO {

    private String nombreCompleto;
    private String telefono;
    private Boolean activo;

    private UsuarioPatchDTO usuario;

    private DomicilioPatchDTO domicilio;
}
