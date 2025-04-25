package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.service.DetalleProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalleProducto")
@RequiredArgsConstructor
public class DetalleProductoController {

    private final DetalleProductoService detalleProductoService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DetalleProductoDTO>> getAllDetallesProducto() {
        return ResponseEntity.ok(detalleProductoService.getAllDetallesProducto());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalleProductoDTO> getDetalleProductoById(@PathVariable Long id) {
        DetalleProductoDTO detalleProductoDTO = detalleProductoService.getDetalleProductoById(id);
        return ResponseEntity.ok(detalleProductoDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetalleProducto(@RequestBody DetalleProductoDTO detalleProductoDTO) {
        detalleProductoService.saveDetalleProducto(detalleProductoDTO);
        return ResponseEntity.ok("Detalle de producto guardado exitosamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDetalleProducto(@PathVariable Long id,@RequestBody DetalleProductoDTO detalleProductoDTO) {
        detalleProductoService.updateDetalleProducto(id, detalleProductoDTO);
        return ResponseEntity.ok("Detalle de producto actualizado exitosamente");
    }



}
