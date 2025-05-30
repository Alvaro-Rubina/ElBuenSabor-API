package org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroInsumoDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;

    private Boolean activo;

    // este campo es opcional
    private Long rubroPadreId;
}
