package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.PromocionDTO;
import org.spdgrupo.elbuensaborapi.service.PromocionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService promocionService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<PromocionDTO>> getPromociones() {
        return ResponseEntity.ok(promocionService.getAllPromociones());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PromocionDTO> getPromocionById(@PathVariable Long id) {
        return ResponseEntity.ok(promocionService.getPromocionById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> savePromocion(@RequestBody PromocionDTO promocionDTO) {
        promocionService.savePromocion(promocionDTO);
        return ResponseEntity.ok("Promocion guardada correctamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePromocion(@PathVariable Long id, @RequestBody PromocionDTO promocionDTO) {
        promocionService.updatePromocion(id, promocionDTO);
        return ResponseEntity.ok("Promocion actualizada correctamente");
    }

}
