package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/detallePedido")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    public ResponseEntity<List<DetallePedidoDTO>> getDetallesPedido() {
        return ResponseEntity.ok(detallePedidoService.getAllDetallesPedido());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoDTO> getDetallePedidoById(Long id) {
        return ResponseEntity.ok(detallePedidoService.getDetallePedidoById(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedidoDTO>> getDetallesPedidoByPedidoId(Long pedidoId) {
        return ResponseEntity.ok(detallePedidoService.getDetallesPedidoByPedidoId(pedidoId));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetallePedido(DetallePedidoDTO detallePedidoDTO) {
        detallePedidoService.saveDetallePedido(detallePedidoDTO);
        return ResponseEntity.ok("Detalle de pedido guardado exitosamente");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateDetallePedido(DetallePedidoDTO detallePedidoDTO, Long id) {
        detallePedidoService.updateDetallePedido(detallePedidoDTO, id);
        return ResponseEntity.ok("Detalle de pedido actualizado exitosamente");
    }


}
