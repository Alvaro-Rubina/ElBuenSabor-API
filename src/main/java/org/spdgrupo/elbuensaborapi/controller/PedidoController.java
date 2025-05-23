package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/save")
    public ResponseEntity<String> savePedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        pedidoService.savePedido(pedidoDTO);
        return ResponseEntity.ok("Pedido guardado correctamente");
    }

    @GetMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<PedidoResponseDTO> getPedidoByCodigoOrden(@PathVariable String codigo) {
        PedidoResponseDTO pedidoDTO = pedidoService.getPedidoByCodigo(codigo);
        return ResponseEntity.ok(pedidoDTO);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    @PatchMapping("/actualizar-estado/{pedidoId}")
    public ResponseEntity<String> actualizarEstadoPedido(@PathVariable Long pedidoId,
                                                         @RequestBody PedidoPatchDTO pedidoDTO) {
        pedidoService.actualizarEstadoDelPedido(pedidoId, pedidoDTO);
        return ResponseEntity.ok("Estado del pedido actualizado correctamente");
    }

    @PatchMapping("/agregar-min/{pedidoId}")
    // El url quedaria algo asi: http//localhost:8080/agregarMin/1?minutos=10
    public ResponseEntity<String> agregarTiempoAlPedido(@PathVariable Long pedidoId,
                                                        @RequestParam Long minutos) {
        pedidoService.agregarTiempoAlPedido(pedidoId, minutos);
        return ResponseEntity.ok("Tiempo agregado al pedido correctamente");
    }

}
