package org.spdgrupo.elbuensaborapi.controller;

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

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<FacturaDTO>> getFacturas() {
        return ResponseEntity.ok(facturaService.getAllFacturas());
    }

    @GetMapping("/factura/{id}")
    @ResponseBody
    public ResponseEntity<FacturaDTO> getFacturaById(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.getFacturaById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveFactura(@RequestBody FacturaDTO facturaDTO) {
        facturaService.saveFactura(facturaDTO);
        return ResponseEntity.ok("Factura guardada correctamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFactura(@PathVariable Long id, @RequestBody FacturaDTO facturaDTO) {
        facturaService.updateFactura(id, facturaDTO);
        return ResponseEntity.ok("Factura actualizada correctamente");
    }

}
