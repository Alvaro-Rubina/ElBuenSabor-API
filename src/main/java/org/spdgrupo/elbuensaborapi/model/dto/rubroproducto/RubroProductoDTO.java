package org.spdgrupo.elbuensaborapi.model.dto.rubroproducto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroProductoDTO {

    @NotBlank (message = "El campo denominacion no puede estar vacio")
    private String denominacion;

    @NotNull(message = "El campo activo no puede ser nulo")
    private boolean activo;
}
