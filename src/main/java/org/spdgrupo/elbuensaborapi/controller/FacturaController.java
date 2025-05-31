package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
