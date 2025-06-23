package org.spdgrupo.elbuensaborapi.model.dto.insumo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsumoVentasDTO {
    private Long id;
    private String denominacion;
    private Long cantidadVendidos;
}
