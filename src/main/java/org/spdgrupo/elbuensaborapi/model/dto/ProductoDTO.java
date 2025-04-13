package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductoDTO {

    private Long id;
    private String denominacion;
    private String descripcion;
    private Double precioVenta;
    private String urlImagen;
    private Boolean activo;
    private RubroProductoDTO rubro;
}
