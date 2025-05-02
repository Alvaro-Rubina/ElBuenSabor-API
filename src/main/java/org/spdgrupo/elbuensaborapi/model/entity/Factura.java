package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import java.time.LocalDate;

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
    private Long numeroComprobante;
    private Double montoDescuento;
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;
    private String numeroTarjeta;
    private String totalVenta;
    private Double costoEnvio;

    // TODO: Después ver el temita de MercadoPagoDatos

}
