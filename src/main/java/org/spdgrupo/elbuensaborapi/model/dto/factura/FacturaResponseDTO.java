package org.spdgrupo.elbuensaborapi.model.dto.factura;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private String numeroTarjeta;
    private String totalVenta;
    private Double montoDescuento;
    private Double costoEnvio;
}
