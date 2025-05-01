package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.FacturaDTO;
import org.spdgrupo.elbuensaborapi.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping("/save")
    public ResponseEntity<String> saveFactura(@Valid @RequestBody FacturaDTO facturaDTO) {
        facturaService.saveFactura(facturaDTO);
        return ResponseEntity.ok("Factura guardada correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<FacturaDTO> getFacturaById(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.getFacturaById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<FacturaDTO>> getFacturas() {
        return ResponseEntity.ok(facturaService.getAllFacturas());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFactura(@PathVariable Long id,@Valid @RequestBody FacturaDTO facturaDTO) {
        facturaService.updateFactura(id, facturaDTO);
        return ResponseEntity.ok("Factura actualizada correctamente");
    }

}
