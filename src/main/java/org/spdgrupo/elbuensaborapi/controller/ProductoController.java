package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController extends GenericoControllerImpl<
        Producto,
        ProductoDTO,
        ProductoResponseDTO,
        Long,
        ProductoService> {

    @Autowired
    private ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        super(productoService);
        this.productoService = productoService;
    }

    @GetMapping("/denominacion/{denominacion}")
    public ResponseEntity<ProductoResponseDTO> findByDenominacion(@PathVariable String denominacion) {
        ProductoResponseDTO producto = productoService.findByDenominacion(denominacion);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> findByDenominacionContaining(@RequestParam String denominacion) {
        return ResponseEntity.ok(productoService.findByDenominacionContaining(denominacion));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProducto(@PathVariable Long id,
                                                 @Valid @RequestBody ProductoDTO productoDTO) {
        productoService.update(id, productoDTO);
        return ResponseEntity.ok("Producto actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }


}
