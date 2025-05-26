package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DetallePedidoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {

    // Dependencias
    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository;
    private final DetallePedidoMapper detallePedidoMapper;

    // MAPPERS
    @Transactional
    public DetallePedido createDetallePedido(DetallePedidoDTO detallePedidoDTO) {
        validarDetallePedido(detallePedidoDTO);

        DetallePedido detallePedido = detallePedidoMapper.toEntity(detallePedidoDTO);

        // Establecer producto o insumo
        if (detallePedidoDTO.getProductoId() != null) {
            detallePedido.setProducto(productoRepository.findById(detallePedidoDTO.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto con el id " + detallePedidoDTO.getProductoId() + " no encontrado")));
        } else {
            detallePedido.setInsumo(insumoRepository.findById(detallePedidoDTO.getInsumoId())
                    .orElseThrow(() -> new NotFoundException("Insumo con el id " + detallePedidoDTO.getInsumoId() + " no encontrado")));
        }

        // Calcular subtotales
        calcularSubtotales(detallePedido);

        return detallePedido;
    }

    public DetallePedidoResponseDTO getDetallePedido(DetallePedido detallePedido) {
        return detallePedidoMapper.toResponseDTO(detallePedido);
    }

    private void validarDetallePedido(DetallePedidoDTO detallePedidoDTO) {
        if ((detallePedidoDTO.getProductoId() == null && detallePedidoDTO.getInsumoId() == null) ||
                (detallePedidoDTO.getProductoId() != null && detallePedidoDTO.getInsumoId() != null)) {
            throw new IllegalArgumentException("Debe haber un producto o un insumo en el DetallePedido, no pueden ser ambos nulos ni tampoco pueden estar ambos");
        }
    }

    private void calcularSubtotales(DetallePedido detallePedido) {
        Double precioVenta = detallePedido.getProducto() != null ?
                detallePedido.getProducto().getPrecioVenta() :
                detallePedido.getInsumo().getPrecioVenta();

        Double precioCosto = detallePedido.getProducto() != null ?
                detallePedido.getProducto().getPrecioCosto() :
                detallePedido.getInsumo().getPrecioCosto();

        detallePedido.setSubTotal(precioVenta * detallePedido.getCantidad());
        detallePedido.setSubTotalCosto(precioCosto * detallePedido.getCantidad());
    }
}
