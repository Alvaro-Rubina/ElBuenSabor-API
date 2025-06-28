package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngresosEgresosDTO {

    private Double ingresos;
    private LocalDate fecha;
    private Double egresos;
    private Double ganancias;
}
