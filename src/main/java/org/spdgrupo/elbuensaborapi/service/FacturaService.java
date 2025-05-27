package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacturaService {

    // Dependencias

    // MAPPERS
    /*public Factura toEntity(FacturaDTO facturaDTO) {
        return Factura.builder()
                .fechaFacturacion(facturaDTO.getFechaFacturacion())
                .horaFacturacion(facturaDTO.getHoraFacturacion()) // Esto será distinto a lo del Pedido
                .codigoComprobante(facturaDTO.getCodigoComprobante()) // Esto será distinto a lo del Pedido
                *//*.totalVenta()
                .montoDescuento()
                .costoEnvio()*//*
                .build();
    }*/

    // TODO: Hacer el metodo para crear la factura

    public FacturaResponseDTO toDTO(Factura factura) {
        return FacturaResponseDTO.builder()
                .build();
    }

}
