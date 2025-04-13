package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InsumoDTO {

    private String denominacion;
    private String urlImagen;
    private Double precioCompra;
    private Double precioVenta;
    private Double stockActual;
    private Double stockMinimo;
    private Boolean esParaElaborar;
    private Boolean activo;
    private UnidadMedida unidadMedida;
    private Long rubroId;
    private List<DetalleFacturaDTO> detallesFactura;
    private List<DetalleProductoDTO> detallesProducto;
    private List<DetallePromocionDTO> detallesPromocion;
}
