package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.DetalleFacturaDTO;
import org.spdgrupo.elbuensaborapi.service.DetalleFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalleFactura")
public class DetalleFacturaController {

    @Autowired
    private DetalleFacturaService detalleFacturaService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetalleFacturaDTO>> getDetallesFactura() {
        return ResponseEntity.ok(detalleFacturaService.getAllDetallesFactura());
    }

    @GetMapping ("/{id}")
    @ResponseBody
    public ResponseEntity<DetalleFacturaDTO> getDetalleFacturaById(@PathVariable Long id) {
        return ResponseEntity.ok(detalleFacturaService.getDetalleFacturaById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetalleFactura(@RequestBody DetalleFacturaDTO detalleFacturaDTO) {
        detalleFacturaService.saveDetalleFactura(detalleFacturaDTO);
        return ResponseEntity.ok("Detalle de factura guardado exitosamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDetalleFactura(@PathVariable Long id,
                                                       @RequestBody DetalleFacturaDTO detalleFacturaDTO) {
        detalleFacturaService.updateDetalleFactura(id, detalleFacturaDTO);
        return ResponseEntity.ok("Detalle de factura actualizado exitosamente");
    }



}
