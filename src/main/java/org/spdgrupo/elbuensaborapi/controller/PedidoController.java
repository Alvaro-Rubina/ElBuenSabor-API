package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController extends GenericoControllerImpl<
        Pedido,
        PedidoDTO,
        PedidoResponseDTO,
        Long,
        PedidoService> {

    @Autowired
    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        super(pedidoService);
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<PedidoResponseDTO> getPedidoByCodigoOrden(@PathVariable String codigo) {
        PedidoResponseDTO pedidoDTO = pedidoService.getPedidoByCodigo(codigo);
        return ResponseEntity.ok(pedidoDTO);
    }

    @PutMapping("/agregar-min/{pedidoId}")
    // El url quedaria algo asi: http//localhost:8080/agregarMin/1?minutos=10
    public ResponseEntity<String> agregarTiempoAlPedido(@PathVariable Long pedidoId,
                                                        @RequestParam Long minutos) {
        pedidoService.agregarTiempoAlPedido(pedidoId, minutos);
        return ResponseEntity.ok("Tiempo agregado al pedido correctamente");
    }

}
