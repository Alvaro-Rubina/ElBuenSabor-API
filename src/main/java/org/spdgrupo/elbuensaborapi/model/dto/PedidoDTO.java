package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "El campo numero no puede ser nulo")
    @Min(value = 1, message = "El campo numero debe ser mayor a 0")
    private Integer numero;
    private Estado estado;
    private LocalTime horaEstimadaFin;
    private TipoEnvio tipoEnvio;
    @NotNull(message = "El campo totalVenta no puede ser nulo")
    @DecimalMin(value = "0.1", message = "El campo totalVenta debe ser mayor a 0")
    private Double totalVenta;
    @NotNull(message = "El campo totalCosto no puede ser nulo")
    @DecimalMin(value = "0.1", message = "El campo totalCosto debe ser mayor a 0")
    private Double totalCosto;
    private FormaPago formaPago;
    private ClienteDTO cliente;
    private DomicilioDTO domicilio;
}
