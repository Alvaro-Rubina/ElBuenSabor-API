package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.InsumoDTO;
import org.spdgrupo.elbuensaborapi.service.InsumoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
public class InsumoController {

    private final InsumoService insumoService;

    @PostMapping("/save")
    public ResponseEntity<String> saveInsumo(@RequestBody InsumoDTO insumoDTO) {
        insumoService.saveInsumo(insumoDTO);
        return ResponseEntity.ok("Insumo guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<InsumoDTO> getInsumoById(@PathVariable Long id) {
        return ResponseEntity.ok(insumoService.getInsumoById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<InsumoDTO>> getAllInsumos() {
        return ResponseEntity.ok(insumoService.getAllInsumos());
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateInsumo(@PathVariable Long id, @RequestBody InsumoDTO insumoDTO) {
        insumoService.updateInsumo(id, insumoDTO);
        return ResponseEntity.ok("Insumo actualizado correctamente");
    }
    
}
