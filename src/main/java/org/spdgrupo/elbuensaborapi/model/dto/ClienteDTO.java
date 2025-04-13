package org.spdgrupo.elbuensaborapi.model.dto;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private Long idUsuario;
    private List<PedidoDTO> pedidos;
    private List<DetalleDomicilioDTO> detallesDomicilio;


}
