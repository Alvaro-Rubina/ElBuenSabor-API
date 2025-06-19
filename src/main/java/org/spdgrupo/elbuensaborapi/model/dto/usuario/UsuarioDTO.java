package org.spdgrupo.elbuensaborapi.model.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioDTO {

    // TODO: Esto tendría que ser opcional? por lo de registrarse con google
    @NotBlank(message = "El campo email no puede estar vacio")
    private String email;

    private String nombreCompleto;

    private String contrasenia;

    private String connection;

    private String nickName;

    private String auth0Id;

    private List<String> roles;
}

