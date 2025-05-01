package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.DetallePedidoDTO;
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
    public ResponseEntity<DetallePedidoDTO> getDetallePedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(detallePedidoService.getDetallePedidoById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetallePedidoDTO>> getDetallesPedido() {
        return ResponseEntity.ok(detallePedidoService.getAllDetallesPedido());
    }

    @GetMapping("/pedido/{pedidoId}")
    @ResponseBody
    public ResponseEntity<List<DetallePedidoDTO>> getDetallesPedidoByPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(detallePedidoService.getDetallesPedidoByPedidoId(pedidoId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDetallePedido(@PathVariable Long id,
                                                      @Valid @RequestBody DetallePedidoDTO detallePedidoDTO) {
        detallePedidoService.updateDetallePedido(id, detallePedidoDTO);
        return ResponseEntity.ok("Detalle de pedido actualizado correctamente");
    }

}
