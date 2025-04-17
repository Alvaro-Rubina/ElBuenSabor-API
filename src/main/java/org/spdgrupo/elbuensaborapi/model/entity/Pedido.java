package org.spdgrupo.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private Integer numero;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private LocalTime horaEstimadaFin;
    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;
    private Double totalVenta;
    private Double totalCosto;
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    // TODO: Después ver el temita de MercadoPagoDatos

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_domicilio")
    private Domicilio domicilio;

}
