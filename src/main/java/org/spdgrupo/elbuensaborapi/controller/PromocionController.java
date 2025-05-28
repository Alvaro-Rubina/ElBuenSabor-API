package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.service.PromocionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService promocionService;

    @PostMapping("/save")
    public ResponseEntity<String> savePromocion(@Valid @RequestBody PromocionDTO promocionDTO) {
        promocionService.savePromocion(promocionDTO);
        return ResponseEntity.ok("Promocion guardada correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PromocionResponseDTO> getPromocionById(@PathVariable Long id) {
        return ResponseEntity.ok(promocionService.getPromocionById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<PromocionResponseDTO>> getPromociones() {
        return ResponseEntity.ok(promocionService.getAllPromociones());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePromocion(@PathVariable Long id,
                                                  @Valid @RequestBody PromocionDTO promocionDTO) {
        promocionService.updatePromocion(id, promocionDTO);
        return ResponseEntity.ok("Promocion actualizada correctamente");
    }

}
