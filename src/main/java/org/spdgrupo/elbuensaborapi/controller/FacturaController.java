package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<FacturaResponseDTO> getFacturaById(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.getFacturaById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<FacturaResponseDTO>> getFacturas() {
        return ResponseEntity.ok(facturaService.getAllFacturas());
    }

}
