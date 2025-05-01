package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RubroProductoDTO {

    private Long id;
    @NotBlank (message = "el campo denominacion no puede estar vacio")
    private String denominacion;
    private boolean activo;
}
