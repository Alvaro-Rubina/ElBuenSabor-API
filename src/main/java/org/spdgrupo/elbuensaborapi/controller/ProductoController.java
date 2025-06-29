package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ProductoController(ProductoService productoService) {
        super(productoService);
        this.productoService = productoService;
    }

    @Override
    @PostMapping("/save")
    public ResponseEntity<ProductoResponseDTO> save(@Valid @RequestBody ProductoDTO productoDTO) {
        ProductoResponseDTO producto = productoService.save(productoDTO);
        messagingTemplate.convertAndSend("/topic/productos", producto);
        return ResponseEntity.ok(producto);
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
    public ResponseEntity<ProductoResponseDTO> updateProducto(@PathVariable Long id,
                                                 @Valid @RequestBody ProductoDTO productoDTO) {
        ProductoResponseDTO producto = productoService.update(id, productoDTO);
        messagingTemplate.convertAndSend("/topic/productos", producto);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        productoService.delete(id);
        messagingTemplate.convertAndSend("/topic/productos", id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }


    @Override
    @PutMapping("/toggle-activo/{id}")
    public ResponseEntity<String> toggleActivo(@PathVariable Long id) {
        String response = productoService.toggleActivo(id);
        messagingTemplate.convertAndSend("/topic/productos", response);
        return ResponseEntity.ok(response);
    }

}
