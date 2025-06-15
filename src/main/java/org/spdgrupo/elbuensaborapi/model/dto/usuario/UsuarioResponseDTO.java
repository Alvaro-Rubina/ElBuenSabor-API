package org.spdgrupo.elbuensaborapi.model.dto.usuario;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String email;
    private String auth0Id;
    private List<String> roles;
}
