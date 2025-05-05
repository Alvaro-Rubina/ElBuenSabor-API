package org.spdgrupo.elbuensaborapi.model.dto.producto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoResponseDTO;

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
    private Double precioVenta;
    private Double precioCosto;
    private String urlImagen;
    private boolean activo;
    private RubroProductoResponseDTO rubro;
    private List<DetalleProductoResponseDTO> detalleProductos;
}
