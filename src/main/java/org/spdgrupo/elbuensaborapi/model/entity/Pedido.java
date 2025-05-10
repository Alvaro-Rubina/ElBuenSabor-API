package org.spdgrupo.elbuensaborapi.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora;

    @Column(unique = true)
    private String codigo;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaEstimadaFin;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;

    private Double totalVenta;

    private Double totalCosto;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_domicilio")
    private Domicilio domicilio;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detallePedidos = new ArrayList<>();

    @OneToOne
    private Factura factura;

}
