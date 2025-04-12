package org.alvarub.elbuensaborapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.alvarub.elbuensaborapi.model.Enums.Estado;
import org.alvarub.elbuensaborapi.model.Enums.FormaPago;
import org.alvarub.elbuensaborapi.model.Enums.TipoEnvio;

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
