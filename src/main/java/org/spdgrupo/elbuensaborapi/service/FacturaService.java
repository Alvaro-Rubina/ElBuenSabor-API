package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.FacturaDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.repository.FacturaRepository;
import org.spdgrupo.elbuensaborapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    public void saveFactura(FacturaDTO facturaDTO) {

    }

    private Factura toEntity(FacturaDTO facturaDTO) {
        return Factura.builder()
                .fecha(LocalDate.now())
                .numeroComprobante(facturaDTO.getNumeroComprobante())
                .montoDescuento(facturaDTO.getMontoDescuento())
                .formaPago(facturaDTO.getFormaPago())
                .numeroTarjeta(facturaDTO.getNumeroTarjeta())
                .totalVenta(facturaDTO.getTotalVenta())
                .costoEnvio(facturaDTO.getCostoEnvio())
                .pedido(pedidoRepository.findById(facturaDTO.getPedidoDTO().getId())
                        .orElseThrow(() -> new NotFoundException("Pedido con el id " + facturaDTO.getPedidoDTO().getId() + " no encontrado")))
                .build();
    }

    private FacturaDTO toDTO(Factura factura) {
        return FacturaDTO.builder()
                .id(factura.getId())
                .fecha(factura.getFecha())
                .numeroComprobante(factura.getNumeroComprobante())
                .montoDescuento(factura.getMontoDescuento())
                .formaPago(factura.getFormaPago())
                .numeroTarjeta(factura.getNumeroTarjeta())
                .totalVenta(factura.getTotalVenta())
                .costoEnvio(factura.getCostoEnvio())
                .pedidoDTO(pedidoService.toDTO(factura.getPedido()))
                .build();
    }

}
