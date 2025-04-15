package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.DetalleFacturaDTO;
import org.spdgrupo.elbuensaborapi.service.DetalleFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/detalleFactura")
public class DetalleFacturaController {

    @Autowired
    private DetalleFacturaService detalleFacturaService;

    @GetMapping
    public ResponseEntity<List<DetalleFacturaDTO>> getDetallesFactura() {
        return ResponseEntity.ok(detalleFacturaService.getAllDetallesFactura());
    }

    @GetMapping ("/{id}")
    public ResponseEntity<DetalleFacturaDTO> getDetalleFacturaById(Long id) {
        return ResponseEntity.ok(detalleFacturaService.getDetalleFacturaById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetalleFactura(DetalleFacturaDTO detalleFacturaDTO) {
        detalleFacturaService.saveDetalleFactura(detalleFacturaDTO);
        return ResponseEntity.ok("Detalle de factura guardado exitosamente");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateDetalleFactura(Long id, DetalleFacturaDTO detalleFacturaDTO) {
        detalleFacturaService.updateDetalleFactura(id, detalleFacturaDTO);
        return ResponseEntity.ok("Detalle de factura actualizado exitosamente");
    }



}
