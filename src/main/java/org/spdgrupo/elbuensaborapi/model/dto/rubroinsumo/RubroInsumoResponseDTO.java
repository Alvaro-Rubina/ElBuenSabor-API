package org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroInsumoResponseDTO {

    private Long id;
    private String denominacion;
    private boolean activo;
    private Long rubroPadreId;
    private List<RubroInsumoResponseDTO> subRubros = new ArrayList<>();
}
