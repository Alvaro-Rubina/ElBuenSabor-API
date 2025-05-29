package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;

import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoDTO;

import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;
import org.spdgrupo.elbuensaborapi.service.RubroInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rubroinsumos")
public class RubroInsumoController extends GenericoControllerImpl<
        RubroInsumo,
        RubroInsumoDTO,
        RubroInsumoResponseDTO,
        Long,
        RubroInsumoService> {

    @Autowired
    private RubroInsumoService rubroInsumoService;

    public RubroInsumoController(RubroInsumoService rubroInsumoService) {
        super(rubroInsumoService);
        this.rubroInsumoService = rubroInsumoService;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRubroInsumo(@PathVariable Long id,
                                                    @Valid @RequestBody RubroInsumoDTO rubroInsumoDTO) {
        rubroInsumoService.update(id, rubroInsumoDTO);
        return ResponseEntity.ok("RubroInsumo actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRubroInsumo(@PathVariable Long id) {
        rubroInsumoService.delete(id);
        return ResponseEntity.ok("RubroInsumo eliminado correctamente");
    }

}