package org.spdgrupo.elbuensaborapi.model.dto.empleado;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioResponseDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmpleadoResponseDTO {

    private Long id;
    private String nombreCompleto;
    private String telefono;
    private boolean activo;
    private UsuarioResponseDTO usuario;
    private DomicilioResponseDTO domicilio;
}
