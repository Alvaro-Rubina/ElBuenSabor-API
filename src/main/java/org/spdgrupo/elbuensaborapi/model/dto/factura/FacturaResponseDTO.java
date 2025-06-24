package org.spdgrupo.elbuensaborapi.model.dto.factura;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacturaResponseDTO {

    private Long id;
    private LocalDate fechaFacturacion;
    private LocalTime horaFacturacion;
    private String numeroComprobante;
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;
    private Double totalVenta;
    private Double montoDescuento;
    private Double costoEnvio;
    private PedidoResponseDTO pedido;
    private ClienteResponseDTO cliente;
    private List<DetalleFacturaResponseDTO> detalleFacturas;
}
