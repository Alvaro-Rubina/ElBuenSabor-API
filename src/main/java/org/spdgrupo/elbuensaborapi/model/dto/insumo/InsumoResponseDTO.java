package org.spdgrupo.elbuensaborapi.model.dto.insumo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InsumoResponseDTO {

    private Long id;
    private String denominacion;
    private String urlImagen;
    private Double precioCompra;
}
