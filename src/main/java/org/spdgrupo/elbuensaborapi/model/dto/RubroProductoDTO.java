package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroProductoDTO {

    private Long id;
    private String denominacion;
    private String unidadMedida;
    private boolean activo;
}
