package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    @PostMapping("/save")
    public ResponseEntity<String> save(@Valid @RequestBody PedidoDTO pedidoDTO) {
        servicio.save(pedidoDTO);
        // TODO: Cambiar el pedidoDTO (PedidoDTO) a que sea un PedidoResponseDTO
        messagingTemplate.convertAndSend("/topic/pedidos", pedidoDTO);
        return ResponseEntity.ok("Registro exitoso");
    }

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

        PedidoResponseDTO pedido = pedidoService.agregarTiempoAlPedido(pedidoId, minutos);
        messagingTemplate.convertAndSend("/topic/pedidos", pedido);
        return ResponseEntity.ok("Tiempo agregado al pedido correctamente");
    }

    @PutMapping("/actualizar-estado/{pedidoId}")
    public ResponseEntity<String> actualizarEstadoPedido(@PathVariable Long pedidoId,
                                                         @RequestParam Estado estado) {

        PedidoResponseDTO pedido = pedidoService.actualizarEstadoDelPedido(pedidoId, estado);
        messagingTemplate.convertAndSend("/topic/pedidos", pedido);
        return ResponseEntity.ok("Estado del pedido actualizado correctamente");
    }

    @GetMapping("/estado")
    public ResponseEntity<List<PedidoResponseDTO>> getPedidosByEstado(@RequestParam Estado estado) {
        List<PedidoResponseDTO> pedidos = pedidoService.getPedidosByEstado(estado);
        return ResponseEntity.ok(pedidos);
    }

}
