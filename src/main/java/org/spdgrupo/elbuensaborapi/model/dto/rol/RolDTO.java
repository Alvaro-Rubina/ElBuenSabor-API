package org.spdgrupo.elbuensaborapi.model.dto.rol;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RolDTO {

    @NotBlank(message = "El campo nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "El campo descripcion no puede estar vacio")
    private String descripcion;

}
