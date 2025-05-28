package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoPatchDTO;
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

    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<ProductoResponseDTO>> getProductosByDenominacion(@RequestParam String denominacion) {
        return ResponseEntity.ok(productoService.getProductosByDenominacion(denominacion));
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
