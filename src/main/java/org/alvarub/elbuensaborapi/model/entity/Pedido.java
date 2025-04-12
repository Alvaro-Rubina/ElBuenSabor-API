package org.alvarub.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.alvarub.elbuensaborapi.model.enums.Estado;
import org.alvarub.elbuensaborapi.model.enums.TipoEnvio;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private Integer numero;
    private Estado estado;
    private LocalTime horaEstimadaFin;
    private TipoEnvio tipoEnvio;
    private Double totalVenta;
    private Double totalCosto;
    private FormaPago formaDePago;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @OneToOne
    private Domicilio domicilio;
    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> detallesPedido;

}
