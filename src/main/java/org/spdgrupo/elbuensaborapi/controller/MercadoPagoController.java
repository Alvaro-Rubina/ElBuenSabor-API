package org.spdgrupo.elbuensaborapi.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.service.MercadoPagoService;
import org.spdgrupo.elbuensaborapi.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "api/mercado-pago")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/create-preference")
    public ResponseEntity<Preference> createPreference(@RequestBody PedidoDTO pedidoDTO) throws MPException, MPApiException {
        Preference preference = mercadoPagoService.createPreference(pedidoDTO);
        return ResponseEntity.ok(preference);
    }

    @PostMapping("/webhook/notification")
    public ResponseEntity<String> mercadoPagoWebhook(@RequestBody Map<String, Object> body) throws MPException, MPApiException {
        if (!mercadoPagoService.handlePayment(body)) {
            return ResponseEntity.badRequest().body("Error al procesar el webhook de Mercado Pago.");
        }

        return ResponseEntity.ok("Webhook de Mercado Pago recibido correctamente");
    }

}
