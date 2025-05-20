package org.spdgrupo.elbuensaborapi.model.dto.pedido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PedidoPatchDTO {

    private Estado estado;
    private TipoEnvio tipoEnvio;
    private FormaPago formaPago;
    private Long clienteId;
    private Long domicilioId;
    private List<DetallePedidoDTO> detallePedidos= new ArrayList<>();
}
