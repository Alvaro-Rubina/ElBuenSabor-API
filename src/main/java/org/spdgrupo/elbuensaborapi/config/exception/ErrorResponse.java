package org.spdgrupo.elbuensaborapi.config.exception;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {

    private String mensaje;
    private String codigo;
    private LocalDateTime timestamp;

}
