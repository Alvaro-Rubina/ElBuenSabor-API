package org.spdgrupo.elbuensaborapi.model.dto.usuario;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioDTO {

    // TODO: Esto tendría que ser opcional? por lo de registrarse con google
    /*@NotBlank(message = "El campo email no puede estar vacio")*/
    private String email;

    private String nombreCompleto;

    // TODO: Esto tendría que ser opcional? por lo de registrarse con google
    /*@NotBlank(message = "El campo contraseña no puede estar vacio")*/
    private String contrasenia;

    // Esto no se si tiene que ir
    private String connection;

    private String auth0Id;

    private List<String> roles;
}

