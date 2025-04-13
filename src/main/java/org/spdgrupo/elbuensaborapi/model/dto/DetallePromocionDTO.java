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
    private PromocionDTO promocion;
    private ProductoDTO producto;
    private InsumoDTO insumo;
}
