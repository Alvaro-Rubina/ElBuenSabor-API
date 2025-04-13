package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePromocion;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;

import java.util.List;

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
    private Long rubroId;
    private List<DetalleProductoDTO> detallesProducto;
    private List<DetalleFacturaDTO> detallesFactura;
    private List<DetallePromocionDTO> detallesPromocion;
}
