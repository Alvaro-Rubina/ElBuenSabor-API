package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroInsumoDTO {

    private Long id;
    @NotBlank(message = "el campo denominacion no puede estar vacio")
    private String denominacion;
    private boolean activo;
    @Enumerated(EnumType.STRING)
    private UnidadMedida unidadMedida;
    private RubroInsumoDTO rubroPadre;
}
