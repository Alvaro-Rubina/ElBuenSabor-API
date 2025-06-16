package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.PedidoWebhook.PedidoWebhookDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping("/pedido")
    public ResponseEntity<String> recibirWebhook(@RequestBody PedidoWebhookDTO data) {
        // Procesamiento de datos
        return ResponseEntity.ok("Webhook recibido correctamente");
    }
}