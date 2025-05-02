package org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroInsumoDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;

    @NotNull(message = "El campo activo no puede ser nulo")
    private boolean activo;

    @NotNull(message = "El campo rubroPadreId no puede ser nulo")
    @Min(value = 1, message = "El campo rubroPadreId no puede ser menor a 1")
    private Long rubroPadreId;
}
