package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {

    // Dependencias
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    // MAPPERS
    @Transactional
    public DetallePedido toEntity(DetallePedidoDTO detallePedidoDTO) {
        Producto producto = detallePedidoDTO.getProductoId() == null ? null : productoRepository.findById(detallePedidoDTO.getProductoId())
                .orElseThrow(() -> new NotFoundException("Producto con el id " + detallePedidoDTO.getProductoId() + " no encontrado"));
        Insumo insumo = detallePedidoDTO.getInsumoId() == null ? null : insumoRepository.findById(detallePedidoDTO.getInsumoId())
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + detallePedidoDTO.getInsumoId() + " no encontrado"));

        if ((producto == null && insumo == null) || (producto != null && insumo != null)) {
            throw new IllegalArgumentException("Debe haber un producto o un insumo en el DetallePedido, no pueden ser ambos nulos ni tampoco pueden estar ambos");
        }

        Double precioVenta = producto != null ? producto.getPrecioVenta() : insumo.getPrecioVenta();
        Double precioCosto = producto != null ? producto.getPrecioCosto() : insumo.getPrecioCosto();

        return DetallePedido.builder()
                .cantidad(detallePedidoDTO.getCantidad())
                .subTotal(precioVenta * detallePedidoDTO.getCantidad())
                .subTotalCosto(precioCosto * detallePedidoDTO.getCantidad())
                .producto(producto)
                .insumo(insumo)
                .build();
    }
    public DetallePedidoResponseDTO toDTO(DetallePedido detallePedido) {
        return DetallePedidoResponseDTO.builder()
                .id(detallePedido.getId())
                .cantidad(detallePedido.getCantidad())
                .subTotal(detallePedido.getSubTotal())
                .subTotalCosto(detallePedido.getSubTotalCosto())
                .producto(detallePedido.getProducto() == null ? null : productoService.toDTO(detallePedido.getProducto()))
                .insumo(detallePedido.getInsumo() == null ? null : insumoService.toDTO(detallePedido.getInsumo()))
                .build();
    }
}
