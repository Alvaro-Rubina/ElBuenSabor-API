package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DetallePedidoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetallePedidoService extends GenericoServiceImpl<DetallePedido, DetallePedidoDTO, DetallePedidoResponseDTO,Long> {

    // Dependencias
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private InsumoRepository insumoRepository;
    @Autowired
    private PromocionRepository promocionRepository;
    @Autowired
    private DetallePedidoMapper detallePedidoMapper;

    public DetallePedidoService(GenericoRepository<DetallePedido, Long> genericoRepository, GenericoMapper<DetallePedido, DetallePedidoDTO, DetallePedidoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }
    
    @Transactional
    public DetallePedido createDetallePedido(DetallePedidoDTO detallePedidoDTO) {
        validarDetallePedido(detallePedidoDTO);

        DetallePedido detallePedido = detallePedidoMapper.toEntity(detallePedidoDTO);

        // Establecer producto, insumo o promocion
        if (detallePedidoDTO.getProductoId() != null) {
            detallePedido.setProducto(productoRepository.findById(detallePedidoDTO.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto con el id " + detallePedidoDTO.getProductoId() + " no encontrado")));
            /*detallePedido.setInsumo(null);*/
            /*detallePedido.setPromocion(null);*/
        } else if (detallePedidoDTO.getInsumoId() != null) {
            detallePedido.setInsumo(insumoRepository.findById(detallePedidoDTO.getInsumoId())
                    .orElseThrow(() -> new NotFoundException("Insumo con el id " + detallePedidoDTO.getInsumoId() + " no encontrado")));
            /*detallePedido.setProducto(null);*/
            /*detallePedido.setPromocion(null);*/
        } else if (detallePedidoDTO.getPromocionId() != null) {
            detallePedido.setPromocion(promocionRepository.findById(detallePedidoDTO.getPromocionId())
                    .orElseThrow(() -> new NotFoundException("Promoción con el id " + detallePedidoDTO.getPromocionId() + " no encontrada")));
            /*detallePedido.setProducto(null);*/
            /*detallePedido.setInsumo(null);*/
        }

        // Calcular subtotales
        calcularSubtotales(detallePedido);

        return detallePedido;
    }

    public DetallePedidoResponseDTO getDetallePedido(DetallePedido detallePedido) {
        return detallePedidoMapper.toResponseDTO(detallePedido);
    }

    private void validarDetallePedido(DetallePedidoDTO detallePedidoDTO) {
        int count = 0;
        if (detallePedidoDTO.getProductoId() != null) count++;
        if (detallePedidoDTO.getInsumoId() != null) count++;
        if (detallePedidoDTO.getPromocionId() != null) count++;

        if (count != 1) {
            throw new IllegalArgumentException("Debe especificar exactamente uno de los siguientes campos en el DetallePedido: productoId, insumoId o promocionId.");
        }
    }

    private void calcularSubtotales(DetallePedido detallePedido) {
        Double precioVenta = null;
        Double precioCosto = null;

        if (detallePedido.getProducto() != null) {
            precioVenta = detallePedido.getProducto().getPrecioVenta();
            precioCosto = detallePedido.getProducto().getPrecioCosto();
        } else if (detallePedido.getInsumo() != null) {
            precioVenta = detallePedido.getInsumo().getPrecioVenta();
            precioCosto = detallePedido.getInsumo().getPrecioCosto();
        } else if (detallePedido.getPromocion() != null) {
            precioVenta = detallePedido.getPromocion().getPrecioVenta();
            precioCosto = detallePedido.getPromocion().getPrecioCosto();
        }

        if (precioVenta == null || precioCosto == null) {
            throw new IllegalStateException("No se pudo determinar el precio de venta o costo para el detalle del pedido.");
        }

        detallePedido.setSubTotal(precioVenta * detallePedido.getCantidad());
        detallePedido.setSubTotalCosto(precioCosto * detallePedido.getCantidad());
    }
}
