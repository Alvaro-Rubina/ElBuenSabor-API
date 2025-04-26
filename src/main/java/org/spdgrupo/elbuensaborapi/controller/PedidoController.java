package org.spdgrupo.elbuensaborapi.controller;


import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.PedidoDTO;
import org.spdgrupo.elbuensaborapi.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/save")
    public ResponseEntity<String> savePedido(@RequestBody PedidoDTO pedidoDTO) {
        pedidoService.savePedido(pedidoDTO);
        return ResponseEntity.ok("Pedido guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PedidoDTO> getPedidoById(@PathVariable Long id) {
        PedidoDTO pedidoDTO = pedidoService.getPedidoById(id);
        return ResponseEntity.ok(pedidoDTO);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<PedidoDTO>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePedido(@PathVariable Long id,
                                               @RequestBody PedidoDTO pedidoDTO) {
        pedidoService.updatePedido(id, pedidoDTO);
        return ResponseEntity.ok("Pedido actualizado correctamente");
    }

    @PutMapping("/agregarMin/{pedidoId}")
    // El url quedaria algo asi: http//localhost:8080/agregarMin/1?minutos=10
    public ResponseEntity<String> agregarTiempoAlPedido(@PathVariable Long pedidoId,
                                                        @RequestParam Long minutos) {
        pedidoService.agregarTiempoAlPedido(pedidoId, minutos);
        return ResponseEntity.ok("Tiempo agregado al pedido");
    }

}
