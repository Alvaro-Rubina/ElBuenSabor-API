package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.interfaces.Vendible;
import org.spdgrupo.elbuensaborapi.repository.DetallePedidoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {

    // Dependencias
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    public void saveDetallesPedidos(List<DetallePedidoDTO> detallePedidoDTOList, Pedido pedido) {
        for (DetallePedidoDTO detallePedidoDTO : detallePedidoDTOList) {
            DetallePedido detallePedido = toEntity(detallePedidoDTO, pedido);

            // de paso seteo los totales del pedido
            pedido.setTotalVenta(pedido.getTotalVenta() + detallePedido.getSubTotal());
            pedido.setTotalCosto(pedido.getTotalCosto() + detallePedido.getSubTotalCosto());
            detallePedidoRepository.save(detallePedido);
        }
    }

    // NOTE: No hay metodo updateDetallePedido ya que una vez concretado un Pedido, no tiene sentido editarlo
    // NOTE: (ya que se supone que hubo una fase para elegir productos, cantidades, etc, antes de concretarlo)

    // MAPPERS
    private DetallePedido toEntity(DetallePedidoDTO detallePedidoDTO, Pedido pedido) {
        Producto producto = detallePedidoDTO.getProductoId() == null ? null : productoRepository.findById(detallePedidoDTO.getProductoId())
                .orElseThrow(() -> new NotFoundException("Producto con el id " + detallePedidoDTO.getProductoId() + " no encontrado"));
        Insumo insumo = detallePedidoDTO.getInsumoId() == null ? null : insumoRepository.findById(detallePedidoDTO.getInsumoId())
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + detallePedidoDTO.getInsumoId() + " no encontrado"));

        if ((producto == null && insumo == null) || (producto != null && insumo != null)) {
            throw new IllegalArgumentException("Debe haber un producto o un insumo en el DetallePedido, no pueden ser ambos nulos ni tampoco pueden estar ambos");
        }

        Double subTotal = producto != null
                ? getSubTotal(producto, detallePedidoDTO.getCantidad())
                : getSubTotal(insumo, detallePedidoDTO.getCantidad());

        Double subTotalCosto = producto != null
                ? getSubTotal(producto, detallePedidoDTO.getCantidad())
                : getSubTotal(insumo, detallePedidoDTO.getCantidad());

        return DetallePedido.builder()
                .cantidad(detallePedidoDTO.getCantidad())
                .subTotal(subTotal)
                .subTotalCosto(subTotalCosto)
                .pedido(pedido)
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
                .producto(productoService.toDTO(detallePedido.getProducto()))
                .insumo(insumoService.toDTO(detallePedido.getInsumo()))
                .build();
    }

    // Metodos adicionales
    public <T extends Vendible> Double getSubTotal(T item, int cantidad) {
        return item.getPrecioVenta() * cantidad;
    }

    public <T extends Vendible> Double getSubTotalCosto(T item, int cantidad ) {
        return item.getPrecioCosto() * cantidad;
    }
}
