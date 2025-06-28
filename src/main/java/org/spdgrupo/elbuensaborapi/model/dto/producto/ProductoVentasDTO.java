package org.spdgrupo.elbuensaborapi.model.dto.producto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoVentasDTO {
    private Long id;
    private String denominacion;
    private Long cantidadVendidos;
}
