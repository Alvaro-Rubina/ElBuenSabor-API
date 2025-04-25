package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.DetallePromocionDTO;
import org.spdgrupo.elbuensaborapi.service.DetallePromocionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detallePromocion")
@RequiredArgsConstructor
public class DetallePromocionController {

    private final DetallePromocionService detallePromocionService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetallePromocionDTO>> getDetallesPromocion() {
        return ResponseEntity.ok(detallePromocionService.getAllDetallesPromocion());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetallePromocionDTO> getDetallePromocionById(@PathVariable Long id) {
        return ResponseEntity.ok(detallePromocionService.getDetallePromocionById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetallePromocion(@RequestBody DetallePromocionDTO detallePromocionDTO) {
        detallePromocionService.saveDetallePromocion(detallePromocionDTO);
        return ResponseEntity.ok("Detalle de promoción guardado exitosamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDetallePromocion(@PathVariable Long id,
                                                         @RequestBody DetallePromocionDTO detallePromocionDTO) {
        detallePromocionService.updateDetallePromocion(id, detallePromocionDTO);
        return ResponseEntity.ok("Detalle de promoción actualizado exitosamente");
    }


}
