package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmpleadoDTO {

    private Long id;
    private String nombreCompleto;
    private String telefono;
    private Boolean activo;
    private UsuarioDTO usuario;
    private DomicilioDTO domicilio;
}
