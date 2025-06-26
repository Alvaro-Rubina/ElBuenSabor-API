package org.spdgrupo.elbuensaborapi.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;
import org.spdgrupo.elbuensaborapi.service.utils.FileService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    @CacheEvict(value = "facturas", allEntries = true)
    @Transactional
    public Factura createFacturaFromPedido(Pedido pedido) {
        Factura factura = Factura.builder()
                .fechaFacturacion(java.time.LocalDate.now())
                .horaFacturacion(java.time.LocalTime.now())
                .codigoComprobante(pedido.getCodigo())
                .formaPago(pedido.getFormaPago())
                .totalVenta(pedido.getTotalVenta())
                .montoDescuento(0.0) // TODO: Esto podria ser util si en algun momentos hay promos o cosas asi
                .costoEnvio(pedido.getCostoEnvio())
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
                    .promocionId(detallePedido.getPromocion() != null ? detallePedido.getPromocion().getId() : null)
                    .build();

            DetalleFactura detalleFactura = detalleFacturaService.createDetalleFactura(detalleFacturaDTO);
            detalleFactura.setFactura(factura);
            factura.getDetalleFacturas().add(detalleFactura);
        });

        return facturaRepository.save(factura);
    }

    @Cacheable(value = "facturas", key = "'findByPedidoId_'+#pedidoId")
    public FacturaResponseDTO findByPedidoId(Long pedidoId) {
        Factura factura = facturaRepository.findByPedido_Id(pedidoId)
                .orElseThrow(() -> new NotFoundException("Factura para el pedido con el id " + pedidoId + " no encontrada"));
        return facturaMapper.toResponseDTO(factura);
    }

    public byte[] exportarFacturaPdf(Long idPedido) throws DocumentException, IOException {
        FacturaResponseDTO factura = findByPedidoId(idPedido);
        return FileService.getFacturaPdf(factura);
    }

    @Override
    @Cacheable("facturas")
    public List<FacturaResponseDTO> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "facturas", key = "'findById_'+#id")
    public FacturaResponseDTO findById(Long id) {
        return super.findById(id);
    }

}