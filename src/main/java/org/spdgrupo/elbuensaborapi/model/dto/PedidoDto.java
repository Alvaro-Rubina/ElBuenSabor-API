package org.spdgrupo.elbuensaborapi.model.dto;

import org.alvarub.elbuensaborapi.model.Enums.Estado;
import org.alvarub.elbuensaborapi.model.Enums.FormaPago;
import org.alvarub.elbuensaborapi.model.Enums.TipoEnvio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PedidoDto {

    private Long id;
    private LocalDate fecha;
    private Integer numero;
    private Estado estado;
    private LocalTime horaEstimadaFin;
    private TipoEnvio tipoEnvio;
    private Double totalVenta;
    private Double totalCosto;
    private FormaPago formaDePago;
    private Long idCliente;
    private Long idDomicilio;
    private List<DetallePedidoDto> detallesPedido;
}
