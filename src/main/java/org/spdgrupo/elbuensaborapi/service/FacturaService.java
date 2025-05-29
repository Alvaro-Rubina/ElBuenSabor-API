package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import org.spdgrupo.elbuensaborapi.config.mappers.FacturaMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FacturaService extends GenericoServiceImpl<Factura, FacturaDTO, FacturaResponseDTO, Long>{

    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private DetalleFacturaService detalleFacturaService;
    @Autowired
    private FacturaMapper facturaMapper;

    public FacturaService(GenericoRepository<Factura, Long> facturaRepository, GenericoMapper<Factura, FacturaDTO, FacturaResponseDTO> genericoMapper) {
        super(facturaRepository, genericoMapper);
    }

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
}