package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
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
    public ResponseEntity<String> saveInsumo(@Valid @RequestBody InsumoDTO insumoDTO) {
        insumoService.saveInsumo(insumoDTO);
        return ResponseEntity.ok("Insumo guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<InsumoResponseDTO> getInsumoById(@PathVariable Long id) {
        return ResponseEntity.ok(insumoService.getInsumoById(id));
    }

    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<InsumoResponseDTO>> getInsumosByDenominacion(@RequestParam String denominacion) {
        return ResponseEntity.ok(insumoService.getInsumosByDenominacion(denominacion));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<InsumoResponseDTO>> getAllInsumos(@RequestParam(required = false) Long rubroId) {
        return ResponseEntity.ok(insumoService.getAllInsumos(rubroId));
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateInsumo(@PathVariable Long id,
                                               @RequestBody InsumoDTO insumoDTO) {
        insumoService.updateInsumo(id, insumoDTO);
        return ResponseEntity.ok("Insumo actualizado correctamente");
    }
    
}
