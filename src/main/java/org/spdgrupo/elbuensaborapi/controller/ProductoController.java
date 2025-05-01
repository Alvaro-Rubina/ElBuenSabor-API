package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping("/save")
    public ResponseEntity<String> saveProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        productoService.saveProducto(productoDTO);
        return ResponseEntity.ok("Producto guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.getProductoById(id));
    }

    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<ProductoDTO>> getProductosByDenominacion(@RequestParam String denominacion) {
        return ResponseEntity.ok(productoService.getProductosByDenominacion(denominacion));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductoDTO>> getAllProductos(@RequestParam(required = false) Long rubroId) {
        return ResponseEntity.ok(productoService.getAllProductos(rubroId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProducto(@PathVariable Long id,@Valid @RequestBody ProductoDTO productoDTO) {
        productoService.updateProducto(id, productoDTO);
        return ResponseEntity.ok("Producto actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok("Producto eliminado exitosamente");
    }


}
