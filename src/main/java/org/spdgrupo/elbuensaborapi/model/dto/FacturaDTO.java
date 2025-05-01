package org.spdgrupo.elbuensaborapi.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "El campo numero de comprobante no puede ser nulo")
    private Long numeroComprobante;
    @NotNull(message = "El campo monto de descuento no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El monto de descuento no puede ser menor a 0")
    private Double montoDescuento;
    private FormaPago formaPago;
    @NotNull(message = "El campo numero de tarjeta no puede ser nulo")
    private String numeroTarjeta;
    @NotBlank(message = "El campo total de venta no puede estar vacio")
    private String totalVenta;
    @NotNull(message = "El campo costo de envio no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El costo de envio no puede ser menor a 0")
    private Double costoEnvio;
    private PedidoDTO pedidoDTO;
}
