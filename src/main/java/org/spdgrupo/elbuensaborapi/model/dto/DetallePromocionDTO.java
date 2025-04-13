package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetallePromocionDTO {

    private Long id;
    private int cantidad;
    private Long promocionId;
    private Long productoId;
    private Long insumoId;
}
