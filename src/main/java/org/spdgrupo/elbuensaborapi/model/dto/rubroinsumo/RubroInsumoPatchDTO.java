package org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo;

import lombok.*;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroInsumoPatchDTO {

    private String denominacion;
    private Boolean activo;
    private Optional<Long> rubroPadreId = Optional.empty();
}
