package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroInsumoDTO {

    private Long id;
    private String denominacion;
    private RubroInsumoDTO rubroPadre;
}
