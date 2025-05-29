package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
public class PromocionController extends GenericoControllerImpl<
        Promocion,
        PromocionDTO,
        PromocionResponseDTO,
        Long,
        PromocionService> {

    @Autowired
    private PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        super(promocionService);
        this.promocionService = promocionService;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        promocionService.delete(id);
        return ResponseEntity.ok("promocion eliminada correctamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePromocion(@PathVariable Long id,
                                                  @Valid @RequestBody PromocionDTO promocionDTO) {
        promocionService.update(id, promocionDTO);
        return ResponseEntity.ok("Promocion actualizada correctamente");
    }


}
