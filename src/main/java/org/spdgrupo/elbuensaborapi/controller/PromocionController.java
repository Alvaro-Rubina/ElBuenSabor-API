package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public PromocionController(PromocionService promocionService) {
        super(promocionService);
        this.promocionService = promocionService;
    }

    @Override
    @PostMapping("/save")
    public ResponseEntity<PromocionResponseDTO> save(@Valid @RequestBody PromocionDTO promocionDTO) {
        PromocionResponseDTO promocion = promocionService.save(promocionDTO);
        messagingTemplate.convertAndSend("/topic/promociones", promocion);
        return ResponseEntity.ok(promocion);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePromocion(@PathVariable Long id) {
        promocionService.delete(id);
        messagingTemplate.convertAndSend("/topic/promociones", id);
        return ResponseEntity.ok("promocion eliminada correctamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePromocion(@PathVariable Long id,
                                                  @Valid @RequestBody PromocionDTO promocionDTO) {
        PromocionResponseDTO promocion = promocionService.update(id, promocionDTO);
        messagingTemplate.convertAndSend("/topic/promociones", promocion);
        return ResponseEntity.ok("Promocion actualizada correctamente");
    }

    @Override
    @PutMapping("/toggle-activo/{id}")
    public ResponseEntity<String> toggleActivo(@PathVariable Long id) {
        String response = promocionService.toggleActivo(id);
        messagingTemplate.convertAndSend("/topic/promociones", response);
        return ResponseEntity.ok(response);
    }


}
