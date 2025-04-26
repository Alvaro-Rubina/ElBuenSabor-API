package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.RubroInsumoDTO;
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
    public ResponseEntity<String> saveRubroInsumo(@RequestBody RubroInsumoDTO rubroInsumoDTO) {
        rubroInsumoService.saveRubroInsumo(rubroInsumoDTO);
        return ResponseEntity.ok("RubroInsumo guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RubroInsumoDTO> getRubroInsumoById(@PathVariable Long id) {
        return ResponseEntity.ok(rubroInsumoService.getRubroInsumoById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<RubroInsumoDTO>> getAllRubroInsumos() {
        return ResponseEntity.ok(rubroInsumoService.getAllRubroInsumos());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRubroInsumo(@PathVariable Long id, @RequestBody RubroInsumoDTO rubroInsumoDTO) {
        rubroInsumoService.updateRubroInsumo(id, rubroInsumoDTO);
        return ResponseEntity.ok("RubroInsumo actualizado correctamente");
    }

}
