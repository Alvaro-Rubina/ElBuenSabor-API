package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.FacturaMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.repository.FacturaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final DetalleFacturaService detalleFacturaService;
    private final FacturaMapper facturaMapper;

    @Transactional
    public Factura createFacturaFromPedido(Pedido pedido) {
        Factura factura = Factura.builder()
                .fechaFacturacion(java.time.LocalDate.now())
                .horaFacturacion(java.time.LocalTime.now())
                .codigoComprobante(pedido.getCodigo())
                .formaPago(pedido.getFormaPago())
                .totalVenta(pedido.getTotalVenta().toString())
                .montoDescuento(0.0)
                .costoEnvio(0.0)
                .pedido(pedido)
                .cliente(pedido.getCliente())
                .detalleFacturas(new ArrayList<>())
                .build();

        // Crear detalles de factura a partir de detalles de pedido
        pedido.getDetallePedidos().forEach(detallePedido -> {
            DetalleFacturaDTO detalleFacturaDTO = DetalleFacturaDTO.builder()
                    .cantidad(detallePedido.getCantidad())
                    .productoId(detallePedido.getProducto() != null ? detallePedido.getProducto().getId() : null)
                    .insumoId(detallePedido.getInsumo() != null ? detallePedido.getInsumo().getId() : null)
                    .build();

            DetalleFactura detalleFactura = detalleFacturaService.createDetalleFactura(detalleFacturaDTO);
            detalleFactura.setFactura(factura);
            factura.getDetalleFacturas().add(detalleFactura);
        });

        return facturaRepository.save(factura);
    }

    public FacturaResponseDTO getFacturaById(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Factura con el id " + id + " no encontrada"));
        return facturaMapper.toResponseDTO(factura);
    }

    public List<FacturaResponseDTO> getAllFacturas() {
        return facturaRepository.findAll().stream()
                .map(facturaMapper::toResponseDTO)
                .toList();
    }
}