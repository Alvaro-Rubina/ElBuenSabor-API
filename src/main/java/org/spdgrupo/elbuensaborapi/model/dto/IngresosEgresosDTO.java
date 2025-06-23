package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngresosEgresosDTO {

    private Double ingresos;
    private Double egresos;
    private Double ganancias;
}
