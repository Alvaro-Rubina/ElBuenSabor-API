package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detallePedido")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetallePedidoDTO>> getDetallesPedido() {
        return ResponseEntity.ok(detallePedidoService.getAllDetallesPedido());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetallePedidoDTO> getDetallePedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(detallePedidoService.getDetallePedidoById(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    @ResponseBody
    public ResponseEntity<List<DetallePedidoDTO>> getDetallesPedidoByPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(detallePedidoService.getDetallesPedidoByPedidoId(pedidoId));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetallePedido(@RequestBody DetallePedidoDTO detallePedidoDTO) {
        detallePedidoService.saveDetallePedido(detallePedidoDTO);
        return ResponseEntity.ok("Detalle de pedido guardado exitosamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDetallePedido(@PathVariable Long id,
                                                      @RequestBody DetallePedidoDTO detallePedidoDTO) {
        detallePedidoService.updateDetallePedido(id, detallePedidoDTO);
        return ResponseEntity.ok("Detalle de pedido actualizado exitosamente");
    }


}
