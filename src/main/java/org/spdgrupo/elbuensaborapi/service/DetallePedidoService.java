package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.repository.DetallePedidoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.PedidoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {

    // Dependencias
    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    public void saveDetallePedido(DetallePedidoDTO detallePedidoDTO) {
        DetallePedido detallePedido = toEntity(detallePedidoDTO);
        detallePedidoRepository.save(detallePedido);
    }

    public DetallePedidoResponseDTO getDetallePedidoById(Long id) {
        DetallePedido detallePedido = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetallePedido con el id " + id + " no encontrado"));
        return toDTO(detallePedido);
    }

    public List<DetallePedidoResponseDTO> getAllDetallesPedido() {
        List<DetallePedido> detallesPedido = detallePedidoRepository.findAll();
        List<DetallePedidoResponseDTO> detallesPedidoDTO = new ArrayList<>();

        for (DetallePedido detallePedido : detallesPedido) {
            DetallePedidoResponseDTO detallePedidoDTO = toDTO(detallePedido);
            detallesPedidoDTO.add(detallePedidoDTO);
        }
        return detallesPedidoDTO;
    }

    // TODO: Ver si este metodo se queda o hacer uno que cumpla la misma funcion en PedidoService
    public List<DetallePedidoResponseDTO> getDetallesPedidoByPedidoId(Long pedidoId) {
        List<DetallePedido> detallesPedido = detallePedidoRepository.findByPedidoId(pedidoId);
        List<DetallePedidoResponseDTO> detallesPedidoDTO = new ArrayList<>();

        for (DetallePedido detallePedido : detallesPedido) {
            DetallePedidoResponseDTO detallePedidoDTO = toDTO(detallePedido);
            detallesPedidoDTO.add(detallePedidoDTO);
        }
        return detallesPedidoDTO;
    }

    // NOTE: No hay metodo updateDetallePedido ya que una vez concretado un Pedido, no tiene sentido editarlo
    // NOTE: (ya que se supone que hubo una fase para elegir productos, cantidades, etc, antes de concretarlo)

    public LocalTime getMayorTiempoEstimado(Long pedidoId) {
        List<DetallePedido> detallesPedido = detallePedidoRepository.findByPedidoId(pedidoId);
        LocalTime mayorTiempo = LocalTime.MIN;

        for (DetallePedido detallePedido : detallesPedido) {
            Producto producto = detallePedido.getProducto();
            if (producto != null) {
                long tiempoEstimado = producto.getTiempoEstimadoPreparacion();
                if (detallePedido.getCantidad() == 2) {
                    tiempoEstimado = Math.round(tiempoEstimado * 1.25);

                } else if (detallePedido.getCantidad() >= 3) {
                    tiempoEstimado = Math.round(tiempoEstimado * 1.5);
                }
                // TODO: Despues ver si a partir de tal cantidad se agrega mas tiempo

                LocalTime tiempoPreparacion = LocalTime.ofSecondOfDay(tiempoEstimado * 60); // Aca convierto minutos a LocalTime
                if (tiempoPreparacion.isAfter(mayorTiempo)) {
                    mayorTiempo = tiempoPreparacion;
                }
            }
        }

        // tiempo de prep = el mayor + 10 minutos
        return mayorTiempo.plusMinutes(10);
    }

    // MAPPERS
    private DetallePedido toEntity(DetallePedidoDTO detallePedidoDTO) {
        return DetallePedido.builder()
                .cantidad(detallePedidoDTO.getCantidad())
                .subTotal(detallePedidoDTO.getSubTotal())
                .pedido(pedidoRepository.findById(detallePedidoDTO.getPedidoId())
                        .orElseThrow(() -> new NotFoundException("Pedido con el id " + detallePedidoDTO.getPedidoId() + " no encontrado")))
                .producto(detallePedidoDTO.getProductoId() == null ? null : productoRepository.findById(detallePedidoDTO.getProductoId())
                        .orElseThrow(() -> new NotFoundException("Producto con el id " + detallePedidoDTO.getProductoId() + " no encontrado")))
                .insumo(detallePedidoDTO.getInsumoId() == null ? null : insumoRepository.findById(detallePedidoDTO.getInsumoId())
                        .orElseThrow(() -> new NotFoundException("Insumo con el id " + detallePedidoDTO.getInsumoId() + " no encontrado")))
                .build();
    }
    public DetallePedidoResponseDTO toDTO(DetallePedido detallePedido) {
        return DetallePedidoResponseDTO.builder()
                .id(detallePedido.getId())
                .cantidad(detallePedido.getCantidad())
                .subTotal(detallePedido.getSubTotal())
                .producto(productoService.toDTO(detallePedido.getProducto()))
                .insumo(insumoService.toDTO(detallePedido.getInsumo()))
                .build();
    }
}
