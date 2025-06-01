package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.service.DetalleDomicilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalledomicilios")
public class DetalleDomicilioController extends GenericoControllerImpl<
        DetalleDomicilio,
        DetalleDomicilioDTO,
        DetalleDomicilioResponseDTO,
        Long,
        DetalleDomicilioService> {

    @Autowired
    private DetalleDomicilioService detalleDomicilioService;

    public DetalleDomicilioController(DetalleDomicilioService detalleDomicilioService) {
        super(detalleDomicilioService);
    }

    // TODO: Ver si este metodo se queda o hacer uno que cumpla la misma funcion en ClienteController
    @GetMapping("/cliente/{clienteId}")
    @ResponseBody
    public ResponseEntity<List<DetalleDomicilioResponseDTO>> getDetallesDomicilioByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(detalleDomicilioService.getDetallesDomicilioByClienteId(clienteId));
    }

}




