package org.alvarub.elbuensaborapi.model.dto;

import org.alvarub.elbuensaborapi.model.enums.Estado;
import org.alvarub.elbuensaborapi.model.enums.FormaPago;
import org.alvarub.elbuensaborapi.model.enums.TipoEnvio;


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
