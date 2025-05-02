package org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroInsumoResponseDTO {

    private Long id;
    private String denominacion;
    private boolean activo;
    private RubroInsumoResponseDTO rubroPadre;
}
