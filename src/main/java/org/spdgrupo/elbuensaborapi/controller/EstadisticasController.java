package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.IngresosEgresosDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoVentasDTO;
import org.spdgrupo.elbuensaborapi.service.InsumoService;
import org.spdgrupo.elbuensaborapi.service.PedidoService;
import org.spdgrupo.elbuensaborapi.service.ProductoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final InsumoService insumoService;

    @GetMapping("/ingresos-egresos")
    public ResponseEntity<IngresosEgresosDTO> obtenerIngresosEgresos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        IngresosEgresosDTO dto = pedidoService.calcularIngresosEgresos(fechaDesde, fechaHasta);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/top-ventas/productos")
    public ResponseEntity<List<ProductoVentasDTO>> obtenerProductosMasVendidos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "10") int limite) {
        List<ProductoVentasDTO> productos = productoService.obtenerProductosMasVendidos(fechaDesde, fechaHasta, limite);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/top-ventas/insumos")
    public ResponseEntity<List<InsumoVentasDTO>> obtenerInsumosMasVendidos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "10") int limite) {
        List<InsumoVentasDTO> insumos = insumoService.obtenerInsumosMasVendidos(fechaDesde, fechaHasta, limite);
        return ResponseEntity.ok(insumos);
    }

}
