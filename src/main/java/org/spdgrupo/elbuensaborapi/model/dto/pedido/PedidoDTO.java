package org.spdgrupo.elbuensaborapi.model.dto.pedido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PedidoDTO {

    @NotNull(message = "El campo tipoEnvio no puede ser nulo")
    private TipoEnvio tipoEnvio;

    @NotNull(message = "El campo formaPago no puede ser nulo")
    private FormaPago formaPago;

    @NotNull(message = "El campo clienteId no puede ser nulo")
    @Min(value = 1, message = "El campo clienteId no puede ser menor a 1")
    private Long clienteId;

    @Min(value = 1, message = "El campo domicilioId no puede ser menor a 1")
    private Long domicilioId;

    @Size(min = 1, message = "El campo detallePedidos debe contener por lo menos 1 elemento")
    private List<DetallePedidoDTO> detallePedidos= new ArrayList<>();

}
