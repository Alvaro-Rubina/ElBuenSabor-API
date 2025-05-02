package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaFacturacion;
    private LocalTime horaFacturacion;
    private Long numeroComprobante;
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;
    private String numeroTarjeta;
    private String totalVenta;
    private Double montoDescuento;
    private Double costoEnvio;

    // TODO: Después ver el temita de MercadoPagoDatos

}
