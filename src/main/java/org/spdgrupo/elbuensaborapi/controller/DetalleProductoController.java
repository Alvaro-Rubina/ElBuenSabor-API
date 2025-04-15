package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.service.DetalleProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/detalleProducto")
public class DetalleProductoController {

    @Autowired
    private DetalleProductoService detalleProductoService;

    @GetMapping
    public ResponseEntity<List<DetalleProductoDTO>> getAllDetallesProducto() {
        return ResponseEntity.ok(detalleProductoService.getAllDetallesProducto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleProductoDTO> getDetalleProductoById(Long id) {
        DetalleProductoDTO detalleProductoDTO = detalleProductoService.getDetalleProductoById(id);
        return ResponseEntity.ok(detalleProductoDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetalleProducto(DetalleProductoDTO detalleProductoDTO) {
        detalleProductoService.saveDetalleProducto(detalleProductoDTO);
        return ResponseEntity.ok("Detalle de producto guardado exitosamente");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateDetalleProducto(Long id, DetalleProductoDTO detalleProductoDTO) {
        detalleProductoService.updateDetalleProducto(id, detalleProductoDTO);
        return ResponseEntity.ok("Detalle de producto actualizado exitosamente");
    }



}
