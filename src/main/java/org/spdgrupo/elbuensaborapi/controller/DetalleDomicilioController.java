package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.service.DetalleDomicilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/detalleDomicilio")
public class DetalleDomicilioController {

    @Autowired
    private DetalleDomicilioService detalleDomicilioService;

    @GetMapping
    public ResponseEntity<List<DetalleDomicilioDTO>> getDetallesDomicilio() {
        return ResponseEntity.ok(detalleDomicilioService.getAllDetallesDomicilio());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleDomicilioDTO> getDetalleDomicilioById(Long id) {
        return ResponseEntity.ok(detalleDomicilioService.getDetalleDomicilioById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetalleDomicilio(DetalleDomicilioDTO detalleDomicilioDTO) {
        detalleDomicilioService.saveDetalleDomicilio(detalleDomicilioDTO);
        return ResponseEntity.ok("Detalle de domicilio guardado exitosamente");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateDetalleDomicilio(DetalleDomicilioDTO detalleDomicilioDTO, Long id) {
        detalleDomicilioService.updateDetalleDomicilio(detalleDomicilioDTO, id);
        return ResponseEntity.ok("Detalle de domicilio actualizado exitosamente");
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<DetalleDomicilioDTO>> getDetallesDomicilioByClienteId(Long clienteId) {
        return ResponseEntity.ok(detalleDomicilioService.getDetallesDomicilioByClienteId(clienteId));
    }


}




