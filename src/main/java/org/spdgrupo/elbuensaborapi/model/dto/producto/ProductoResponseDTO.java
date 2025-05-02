package org.spdgrupo.elbuensaborapi.model.dto.producto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductoResponseDTO {

    private Long id;
    private String denominacion;
    private String descripcion;
    private Long tiempoEstimadoPreparacion;
    private Long precioVenta;
    private String urlImagen;
    private boolean activo;
    private RubroProductoDTO rubro;
    private List<DetalleProductoResponseDTO> detalleProductos;
}
