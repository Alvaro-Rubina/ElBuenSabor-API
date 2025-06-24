package org.spdgrupo.elbuensaborapi.controller;

import com.itextpdf.text.DocumentException;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController extends GenericoControllerImpl<
        Factura,
        FacturaDTO,
        FacturaResponseDTO,
        Long,
        FacturaService> {

    @Autowired
    private FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        super(facturaService);
        this.facturaService = facturaService;
    }

    @GetMapping("/pdf/{pedidoId}")
    ResponseEntity<byte[]> exportarFacturaPdf(@PathVariable Long pedidoId) throws DocumentException, IOException {
        byte[] pdfBytes = facturaService.exportarFacturaPdf(pedidoId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"factura.pdf\"")
                .body(pdfBytes);
    }
}
