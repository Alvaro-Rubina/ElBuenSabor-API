package org.alvarub.elbuensaborapi.model.dto;


import lombok.*;
import org.alvarub.elbuensaborapi.model.entity.DetalleDomicilio;
import org.alvarub.elbuensaborapi.model.entity.Pedido;
import org.alvarub.elbuensaborapi.model.entity.Usuario;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private Long idUsuario;
    private List<PedidoDto> pedidos;
    private List<DetalleDomicilioDto> detallesDomicilio;


}
