package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacturaDTO {

    private Long id;
    private LocalDate fecha;
    private Long numeroComprobante;
    private Double montoDescuento;
    private FormaPago formaPago;
    private String numeroTarjeta;
    private String totalVenta;
    private Double costoEnvio;
    private PedidoDTO pedidoDTO;
}
