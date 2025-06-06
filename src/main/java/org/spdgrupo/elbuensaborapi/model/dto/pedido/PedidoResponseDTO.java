package org.spdgrupo.elbuensaborapi.model.dto.pedido;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PedidoResponseDTO {

    private Long id;
    private LocalDate fecha;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora;
    private String codigo; //PED-2505-00001
    private Estado estado;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaEstimadaFin;
    private TipoEnvio tipoEnvio;
    private Double totalVenta;
    private Double totalCosto;
    private FormaPago formaPago;
    private String comentario;
    private ClienteResponseDTO cliente;
    private DomicilioResponseDTO domicilio;
    private List<DetallePedidoResponseDTO> detallePedidos;
}
