package org.alvarub.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "factura")
    private List<DetalleFactura> detallesFactura;


}
