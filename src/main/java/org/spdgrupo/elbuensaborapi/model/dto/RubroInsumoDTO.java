package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroInsumoDTO {

    private Long id;
    private String denominacion;
    private boolean activo;
    @Enumerated(EnumType.STRING)
    private UnidadMedida unidadMedida;
    private RubroInsumoDTO rubroPadre;
}
