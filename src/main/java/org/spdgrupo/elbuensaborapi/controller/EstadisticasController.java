package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.IngresosEgresosDTO;
import org.spdgrupo.elbuensaborapi.service.PedidoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public final class EstadisticasController {

    private final PedidoService pedidoService;

    @GetMapping("/ingresos-egresos")
    public ResponseEntity<IngresosEgresosDTO> obtenerIngresosEgresos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        IngresosEgresosDTO dto = pedidoService.calcularIngresosEgresos(fechaDesde, fechaHasta);
        return ResponseEntity.ok(dto);
    }

}
