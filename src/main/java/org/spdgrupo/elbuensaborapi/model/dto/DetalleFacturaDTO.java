package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetalleFacturaDTO {

    private Long id;
    private int cantidad;
    private Double subTotal;
    private FacturaDTO factura;
    private ProductoDTO producto;
    private InsumoDTO insumo;
}
