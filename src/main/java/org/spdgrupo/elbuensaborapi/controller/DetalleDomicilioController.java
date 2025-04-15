package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.service.DetalleDomicilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalleDomicilio")
public class DetalleDomicilioController {

    @Autowired
    private DetalleDomicilioService detalleDomicilioService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetalleDomicilioDTO>> getDetallesDomicilio() {
        return ResponseEntity.ok(detalleDomicilioService.getAllDetallesDomicilio());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalleDomicilioDTO> getDetalleDomicilioById(@PathVariable Long id) {
        return ResponseEntity.ok(detalleDomicilioService.getDetalleDomicilioById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetalleDomicilio(@RequestBody DetalleDomicilioDTO detalleDomicilioDTO) {
        detalleDomicilioService.saveDetalleDomicilio(detalleDomicilioDTO);
        return ResponseEntity.ok("Detalle de domicilio guardado exitosamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDetalleDomicilio(@PathVariable Long id,
                                                         @RequestBody DetalleDomicilioDTO detalleDomicilioDTO) {
        detalleDomicilioService.updateDetalleDomicilio(id, detalleDomicilioDTO);
        return ResponseEntity.ok("Detalle de domicilio actualizado exitosamente");
    }

    @GetMapping("/cliente/{clienteId}")
    @ResponseBody
    public ResponseEntity<List<DetalleDomicilioDTO>> getDetallesDomicilioByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(detalleDomicilioService.getDetallesDomicilioByClienteId(clienteId));
    }


}




