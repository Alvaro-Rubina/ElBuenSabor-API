package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DetalleFacturaMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetalleFacturaService extends GenericoServiceImpl<DetalleFactura, DetalleFacturaDTO, DetalleFacturaResponseDTO, Long> {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private InsumoRepository insumoRepository;
    @Autowired
    private PromocionRepository promocionRepository;
    @Autowired
    private DetalleFacturaMapper detalleFacturaMapper;

    public DetalleFacturaService(GenericoRepository<DetalleFactura, Long> genericoRepository, GenericoMapper<DetalleFactura, DetalleFacturaDTO, DetalleFacturaResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Transactional
    public DetalleFactura createDetalleFactura(DetalleFacturaDTO detalleFacturaDTO) {
        validarDetalleFactura(detalleFacturaDTO);

        DetalleFactura detalleFactura = detalleFacturaMapper.toEntity(detalleFacturaDTO);

        // Establecer producto, insumo o promocion
        if (detalleFacturaDTO.getProductoId() != null) {
            detalleFactura.setProducto(productoRepository.findById(detalleFacturaDTO.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto con el id " + detalleFacturaDTO.getProductoId() + " no encontrado")));
            /*detallePedido.setInsumo(null);*/
            /*detallePedido.setPromocion(null);*/
        } else if (detalleFacturaDTO.getInsumoId() != null) {
            detalleFactura.setInsumo(insumoRepository.findById(detalleFacturaDTO.getInsumoId())
                    .orElseThrow(() -> new NotFoundException("Insumo con el id " + detalleFacturaDTO.getInsumoId() + " no encontrado")));
            /*detallePedido.setProducto(null);*/
            /*detallePedido.setPromocion(null);*/
        } else if (detalleFacturaDTO.getPromocionId() != null) {
            detalleFactura.setPromocion(promocionRepository.findById(detalleFacturaDTO.getPromocionId())
                    .orElseThrow(() -> new NotFoundException("Promoción con el id " + detalleFacturaDTO.getInsumoId() + " no encontrada")));
            /*detallePedido.setProducto(null);*/
            /*detallePedido.setInsumo(null);*/
        }

        calcularSubtotales(detalleFactura);

        return detalleFactura;
    }

    public DetalleFacturaResponseDTO getDetalleFactura(DetalleFactura detalleFactura) {
        return detalleFacturaMapper.toResponseDTO(detalleFactura);
    }

    private void validarDetalleFactura(DetalleFacturaDTO detalleFacturaDTO) {
        int count = 0;
        if (detalleFacturaDTO.getProductoId() != null) count++;
        if (detalleFacturaDTO.getInsumoId() != null) count++;
        if (detalleFacturaDTO.getPromocionId() != null) count++;

        if (count != 1) {
            throw new IllegalArgumentException("Debe especificar exactamente uno de los siguientes campos en el DetalleFactura: productoId, insumoId o promocionId.");
        }
    }

    private void calcularSubtotales(DetalleFactura detalleFactura) {
        Double precioVenta = null;
        Double precioCosto = null;

        if (detalleFactura.getProducto() != null) {
            precioVenta = detalleFactura.getProducto().getPrecioVenta();
            precioCosto = detalleFactura.getProducto().getPrecioCosto();
        } else if (detalleFactura.getInsumo() != null) {
            precioVenta = detalleFactura.getInsumo().getPrecioVenta();
            precioCosto = detalleFactura.getInsumo().getPrecioCosto();
        } else if (detalleFactura.getPromocion() != null) {
            precioVenta = detalleFactura.getPromocion().getPrecioVenta();
            precioCosto = detalleFactura.getPromocion().getPrecioCosto();
        }

        if (precioVenta == null || precioCosto == null) {
            throw new IllegalStateException("No se pudo determinar el precio de venta o costo para el detalle del pedido.");
        }

        detalleFactura.setSubTotal(precioVenta * detalleFactura.getCantidad());
        detalleFactura.setSubTotalCosto(precioCosto * detalleFactura.getCantidad());
    }
}