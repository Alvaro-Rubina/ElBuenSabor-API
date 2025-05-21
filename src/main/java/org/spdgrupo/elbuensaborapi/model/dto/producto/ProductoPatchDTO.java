package org.spdgrupo.elbuensaborapi.model.dto.producto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductoPatchDTO {

    private String denominacion;
    private String descripcion;
    private Long tiempoEstimadoPreparacion;
    private Double precioVenta;
    private String urlImagen;
    private Boolean activo;
    private Long rubroId;
    private List<DetalleProductoDTO> detalleProductos;
}
