package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.service.DetallePedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detallePedido")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @PostMapping("/save")
    public ResponseEntity<String> saveDetallePedido(@Valid @RequestBody DetallePedidoDTO detallePedidoDTO) {
        detallePedidoService.saveDetallePedido(detallePedidoDTO);
        return ResponseEntity.ok("Detalle de pedido guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetallePedidoResponseDTO> getDetallePedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(detallePedidoService.getDetallePedidoById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetallePedidoResponseDTO>> getDetallesPedido() {
        return ResponseEntity.ok(detallePedidoService.getAllDetallesPedido());
    }

    // TODO: Ver si este metodo se queda o hacer uno que cumpla la misma funcion en PedidoController
    @GetMapping("/pedido/{pedidoId}")
    @ResponseBody
    public ResponseEntity<List<DetallePedidoResponseDTO>> getDetallesPedidoByPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(detallePedidoService.getDetallesPedidoByPedidoId(pedidoId));
    }

}
