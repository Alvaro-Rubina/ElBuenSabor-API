package org.spdgrupo.elbuensaborapi.model.dto.cliente;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioResponseDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClienteResponseDTO {

    private Long id;
    private String nombreCompleto;
    private String telefono;
    private boolean activo;
    private UsuarioResponseDTO usuario;
    private List<DetalleDomicilioResponseDTO> detalleDomicilios;
}
