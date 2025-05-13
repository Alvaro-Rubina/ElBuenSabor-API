package org.spdgrupo.elbuensaborapi.model.dto.factura;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacturaDTO {

    private LocalDate fechaFacturacion;
    private LocalTime horaFacturacion;
    private Long numeroComprobante;
    private String totalVenta;
    private Double montoDescuento;
    private Double costoEnvio;

    // TODO: Luego ver el tema de MercadoPagoDatos

}
