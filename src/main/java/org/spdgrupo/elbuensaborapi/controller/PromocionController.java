package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionPatchDTO;
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


}
