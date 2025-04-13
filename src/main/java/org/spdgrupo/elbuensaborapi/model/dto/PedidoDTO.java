package org.spdgrupo.elbuensaborapi.model.dto;

import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PedidoDTO {

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
    private List<DetallePedidoDTO> detallesPedido;
}
