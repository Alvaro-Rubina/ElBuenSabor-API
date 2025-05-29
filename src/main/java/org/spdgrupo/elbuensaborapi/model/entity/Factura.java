package org.spdgrupo.elbuensaborapi.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class Factura extends Base {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFacturacion;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaFacturacion;

    @Column(unique = true)
    private String codigoComprobante;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    private String totalVenta;

    private Double montoDescuento;

    private Double costoEnvio;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalleFacturas = new ArrayList<>();

}
