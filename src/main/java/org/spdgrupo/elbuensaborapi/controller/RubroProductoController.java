package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.service.RubroProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rubroproductos")
public class RubroProductoController extends GenericoControllerImpl<RubroProducto,RubroProductoDTO, RubroProductoResponseDTO, Long, RubroProductoService> {
    @Autowired
    private RubroProductoService rubroProductoService;

    public RubroProductoController(RubroProductoService rubroProductoService) {
        super(rubroProductoService);
        this.rubroProductoService = rubroProductoService;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RubroProductoResponseDTO> updateRubroProducto(@PathVariable Long id,
                                                      @Valid @RequestBody RubroProductoDTO rubroProductoDTO) {
        RubroProductoResponseDTO rubroProducto = rubroProductoService.update(id, rubroProductoDTO);
        return ResponseEntity.ok(rubroProducto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRubroProducto(@PathVariable Long id) {
        rubroProductoService.delete(id);
        return ResponseEntity.ok("RubroProducto eliminado correctamente");
    }

}