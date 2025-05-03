package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.service.RubroInsumoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rubroinsumo")
@RequiredArgsConstructor
public class RubroInsumoController {

    private final RubroInsumoService rubroInsumoService;

    @PostMapping("/save")
    public ResponseEntity<String> saveRubroInsumo(@Valid @RequestBody RubroInsumoDTO rubroInsumoDTO) {
        rubroInsumoService.saveRubroInsumo(rubroInsumoDTO);
        return ResponseEntity.ok("RubroInsumo guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RubroInsumoResponseDTO> getRubroInsumoById(@PathVariable Long id) {
        return ResponseEntity.ok(rubroInsumoService.getRubroInsumoById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<RubroInsumoResponseDTO>> getAllRubroInsumos() {
        return ResponseEntity.ok(rubroInsumoService.getAllRubroInsumos());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRubroInsumo(@PathVariable Long id,@Valid @RequestBody RubroInsumoDTO rubroInsumoDTO) {
        rubroInsumoService.updateRubroInsumo(id, rubroInsumoDTO);
        return ResponseEntity.ok("RubroInsumo actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRubroInsumo(@PathVariable Long id) {
        rubroInsumoService.deleteRubroInsumo(id);
        return ResponseEntity.ok("RubroInsumo eliminado correctamente");
    }

}
