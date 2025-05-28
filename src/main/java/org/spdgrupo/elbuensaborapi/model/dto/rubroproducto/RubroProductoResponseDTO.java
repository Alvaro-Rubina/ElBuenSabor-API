package org.spdgrupo.elbuensaborapi.model.dto.rubroproducto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroProductoResponseDTO {

    private Long id;
    private String denominacion;
    private Boolean activo;
}
