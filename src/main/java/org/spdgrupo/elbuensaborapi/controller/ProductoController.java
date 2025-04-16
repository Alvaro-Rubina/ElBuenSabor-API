package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.getProductoById(id));
    }

    @GetMapping("/denominacion/{denominacion}")
    @ResponseBody
    public ResponseEntity<List<ProductoDTO>> getProductosByDenominacion(@PathVariable String denominacion) {
        return ResponseEntity.ok(productoService.getProductosByDenominacion(denominacion));
    }

    @GetMapping("/categoria/{categoria}")
    @ResponseBody
    public ResponseEntity<List<ProductoDTO>> getProductosByDenominacionCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.getProductosByDenominacionCategoria(categoria));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveProducto(@RequestBody ProductoDTO productoDTO) {
        productoService.saveProducto(productoDTO);
        return ResponseEntity.ok("Producto guardado correctamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        productoService.updateProducto(id, productoDTO);
        return ResponseEntity.ok("Producto actualizado correctamente");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok("Producto eliminado exitosamente");
    }


}
