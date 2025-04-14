package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.FacturaDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.repository.FacturaRepository;
import org.spdgrupo.elbuensaborapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    public void saveFactura(FacturaDTO facturaDTO) {
        Factura factura = toEntity(facturaDTO);
        facturaRepository.save(factura);
    }

    public FacturaDTO getFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Factura con el id " + id + " no encontrada"));
        return toDTO(factura);
    }

    public List<FacturaDTO> getAllFacturas() {
        List<Factura> facturas = facturaRepository.findAll();
        List<FacturaDTO> facturasDTO = new ArrayList<>();
        for (Factura factura : facturas) {
            facturasDTO.add(toDTO(factura));
        }
        return facturasDTO;
    }

    public void editFactura(Long id, FacturaDTO facturaDTO) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Factura con id " + id + " no encontrada"));

        if (!factura.getFecha().equals(facturaDTO.getFecha())) {
            factura.setFecha(facturaDTO.getFecha());
        }

        if (!factura.getNumeroComprobante().equals(facturaDTO.getNumeroComprobante())) {
            factura.setNumeroComprobante(facturaDTO.getNumeroComprobante());
        }

        if (!factura.getMontoDescuento().equals(facturaDTO.getMontoDescuento())) {
            factura.setMontoDescuento(factura.getMontoDescuento());
        }

        if (!factura.getFormaPago().equals(facturaDTO.getFormaPago())) {
            factura.setFormaPago(facturaDTO.getFormaPago());
        }

        if (!factura.getNumeroTarjeta().equals(facturaDTO.getNumeroTarjeta())) {
            factura.setNumeroTarjeta(factura.getNumeroTarjeta());
        }

        if (!factura.getTotalVenta().equals(facturaDTO.getTotalVenta())) {
            factura.setTotalVenta(factura.getTotalVenta());
        }

        if (!factura.getCostoEnvio().equals(facturaDTO.getCostoEnvio())) {
            factura.setCostoEnvio(factura.getCostoEnvio());
        }

        if (!factura.getPedido().getId().equals(facturaDTO.getPedidoDTO().getId())) {
            factura.setPedido(pedidoRepository.findById(facturaDTO.getPedidoDTO().getId())
                    .orElseThrow(() -> new NotFoundException("Pedido con el id " + id + " no encontrado")));
        }

        facturaRepository.save(factura);
    }

    // MAPPERS
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

    public FacturaDTO toDTO(Factura factura) {
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
