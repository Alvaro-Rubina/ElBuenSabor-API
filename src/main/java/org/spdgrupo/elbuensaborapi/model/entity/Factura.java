package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    private Long numeroComprobante;
    private Double montoDescuento;
    private FormaPago formaPago;
    private String numeroTarjeta;
    private String totalVenta;
    private Double costoEnvio;

    @OneToOne
    private Pedido pedido;

}
