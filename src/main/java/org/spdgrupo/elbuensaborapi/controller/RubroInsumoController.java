package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.service.RubroInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rubroinsumo")
public class RubroInsumoController {

    @Autowired
    private RubroInsumoService rubroInsumoService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<RubroInsumoDTO>> getAllRubroInsumos() {
        return ResponseEntity.ok(rubroInsumoService.getAllRubroInsumos());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RubroInsumoDTO> getRubroInsumoById(@PathVariable Long id) {
        return ResponseEntity.ok(rubroInsumoService.getRubroInsumoById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveRubroInsumo(@RequestBody RubroInsumoDTO rubroInsumoDTO) {
        rubroInsumoService.saveRubroInsumo(rubroInsumoDTO);
        return ResponseEntity.ok("RubroInsumo guardado correctamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRubroInsumo(@PathVariable Long id, @RequestBody RubroInsumoDTO rubroInsumoDTO) {
        rubroInsumoService.updateRubroInsumo(id, rubroInsumoDTO);
        return ResponseEntity.ok("RubroInsumo actualizado correctamente");
    }

}
