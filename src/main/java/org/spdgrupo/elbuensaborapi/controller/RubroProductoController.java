package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.service.RubroProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rubroProducto")
@RequiredArgsConstructor
public class RubroProductoController {

    private final RubroProductoService rubroProductoService;

    @PostMapping("/save")
    public ResponseEntity<String> saveRubroProducto(@RequestBody RubroProductoDTO rubroProductoDTO) {
        rubroProductoService.saveRubroProducto(rubroProductoDTO);
        return ResponseEntity.ok("RubroProducto guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RubroProductoDTO> getRubroProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(rubroProductoService.getRubroProductoById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<RubroProductoDTO>> getAllRubroProductos() {
        return ResponseEntity.ok(rubroProductoService.getAllRubroProductos());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRubroProducto(@PathVariable Long id, @RequestBody RubroProductoDTO rubroProductoDTO) {
        rubroProductoService.updateRubroProducto(id, rubroProductoDTO);
        return ResponseEntity.ok("RubroProducto actualizado correctamente");
    }

}
