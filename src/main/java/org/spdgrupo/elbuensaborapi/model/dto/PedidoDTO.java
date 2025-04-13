package org.spdgrupo.elbuensaborapi.model.dto;

import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PedidoDTO {

    private Long id;
    private LocalDate fecha;
    private Integer numero;
    private Estado estado;
    private LocalTime horaEstimadaFin;
    private TipoEnvio tipoEnvio;
    private Double totalVenta;
    private Double totalCosto;
    private FormaPago formaPago;
    private ClienteDTO cliente;
    private DomicilioDTO domicilio;
}
