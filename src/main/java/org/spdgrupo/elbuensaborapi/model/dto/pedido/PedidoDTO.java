package org.spdgrupo.elbuensaborapi.model.dto.pedido;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
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

    // este campo es opcional, si no se pasa, el valor x default es SOLICITADO
    // dejo este campo ya que este dto se usara para editar pedidos, x ende es necesario
    private Estado estado = Estado.SOLICITADO;
    private TipoEnvio tipoEnvio;

    @NotNull(message = "El campo totalVenta no puede ser nulo")
    @DecimalMin(value = "0.1", message = "El campo totalVenta debe ser mayor a 0")
    private Double totalVenta;

    @NotNull(message = "El campo totalCosto no puede ser nulo")
    @DecimalMin(value = "0.1", message = "El campo totalCosto debe ser mayor a 0")
    private Double totalCosto;

    @NotNull(message = "El campo formaPago no puede ser nulo")
    private FormaPago formaPago;

    @NotNull(message = "El campo clienteId no puede ser nulo")
    @Min(value = 1, message = "El campo clienteId no puede ser menor a 1")
    private Long clienteId;

    @NotNull(message = "El campo domicilioId no puede ser nulo")
    @Min(value = 1, message = "El campo domicilioId no puede ser menor a 1")
    private Long domicilioId;

}
