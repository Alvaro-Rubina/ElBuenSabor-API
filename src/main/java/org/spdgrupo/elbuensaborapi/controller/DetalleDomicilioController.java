package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.service.DetalleDomicilioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalleDomicilio")
@RequiredArgsConstructor
public class DetalleDomicilioController {

    private final DetalleDomicilioService detalleDomicilioService;

    @PostMapping("/save")
    public ResponseEntity<String> saveDetalleDomicilio(@Valid @RequestBody DetalleDomicilioDTO detalleDomicilioDTO) {
        detalleDomicilioService.saveDetalleDomicilio(detalleDomicilioDTO);
        return ResponseEntity.ok("Detalle de domicilio guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalleDomicilioDTO> getDetalleDomicilioById(@PathVariable Long id) {
        return ResponseEntity.ok(detalleDomicilioService.getDetalleDomicilioById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetalleDomicilioDTO>> getDetallesDomicilio() {
        return ResponseEntity.ok(detalleDomicilioService.getAllDetallesDomicilio());
    }

    @GetMapping("/cliente/{clienteId}")
    @ResponseBody
    public ResponseEntity<List<DetalleDomicilioDTO>> getDetallesDomicilioByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(detalleDomicilioService.getDetallesDomicilioByClienteId(clienteId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDetalleDomicilio(@PathVariable Long id,
                                                         @Valid @RequestBody DetalleDomicilioDTO detalleDomicilioDTO) {
        detalleDomicilioService.updateDetalleDomicilio(id, detalleDomicilioDTO);
        return ResponseEntity.ok("Detalle de domicilio actualizado correctamente");
    }

}




