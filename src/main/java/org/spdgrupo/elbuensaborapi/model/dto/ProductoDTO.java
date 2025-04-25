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
    private Long tiempoEstimadoPreparacion;
    private Double precioVenta;
    private String urlImagen;
    private boolean activo;
    private RubroProductoDTO rubro;
}
