package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.service.RubroProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rubroProducto")
public class RubroProductoController {

    @Autowired
    private RubroProductoService rubroProductoService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<RubroProductoDTO>> getAllRubroProductos() {
        return ResponseEntity.ok(rubroProductoService.getAllRubroProductos());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RubroProductoDTO> getRubroProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(rubroProductoService.getRubroProductoById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveRubroProducto(@RequestBody RubroProductoDTO rubroProductoDTO) {
        rubroProductoService.saveRubroProducto(rubroProductoDTO);
        return ResponseEntity.ok("RubroProducto guardado correctamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRubroProducto(@PathVariable Long id, @RequestBody RubroProductoDTO rubroProductoDTO) {
        rubroProductoService.updateRubroProducto(id, rubroProductoDTO);
        return ResponseEntity.ok("RubroProducto actualizado correctamente");
    }

}
