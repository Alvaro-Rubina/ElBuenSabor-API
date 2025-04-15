package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.repository.DetallePedidoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.PedidoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProductoService productoService;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    private InsumoService insumoService;
    @Autowired
    private InsumoRepository insumoRepository;

    public void saveDetallePedido(DetallePedidoDTO detallePedidoDTO) {
        DetallePedido detallePedido = toEntity(detallePedidoDTO);
        detallePedidoRepository.save(detallePedido);
    }

    public void updateDetallePedido(Long id, DetallePedidoDTO detallePedidoDTO) {
        DetallePedido detallePedido = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado"));

        if (!detallePedidoDTO.getCantidad().equals(detallePedido.getCantidad())) {
            detallePedido.setCantidad(detallePedidoDTO.getCantidad());
        }
        if (!detallePedidoDTO.getSubTotal().equals(detallePedido.getSubTotal())) {
            detallePedido.setSubTotal(detallePedidoDTO.getSubTotal());
        }
        if (!detallePedidoDTO.getProducto().getId().equals(detallePedido.getProducto().getId())) {
            detallePedido.setProducto(productoRepository.findById(detallePedidoDTO.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado")));
        }
        if (!detallePedidoDTO.getInsumo().getId().equals(detallePedido.getInsumo().getId())) {
            detallePedido.setInsumo(insumoRepository.findById(detallePedidoDTO.getInsumo().getId())
                    .orElseThrow(() -> new RuntimeException("Insumo no encontrado")));
        }
        if (!detallePedidoDTO.getPedido().getId().equals(detallePedido.getPedido().getId())) {
            detallePedido.setPedido(pedidoRepository.findById(detallePedidoDTO.getPedido().getId())
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado")));
        }

        detallePedidoRepository.save(detallePedido);
    }

    public DetallePedidoDTO getDetallePedidoById(Long id) {
        DetallePedido detallePedido = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado"));
        return toDto(detallePedido);
    }

    public List<DetallePedidoDTO> getAllDetallesPedido() {
        List<DetallePedido> detallesPedido = detallePedidoRepository.findAll();
        List<DetallePedidoDTO> detallesPedidoDTO = new ArrayList<>();
        for (DetallePedido detallePedido : detallesPedido) {
            DetallePedidoDTO detallePedidoDTO = toDto(detallePedido);
            detallesPedidoDTO.add(detallePedidoDTO);
        }
        return detallesPedidoDTO;
    }

    public List<DetallePedidoDTO> getDetallesPedidoByPedidoId(Long pedidoId) {
        List<DetallePedido> detallesPedido = detallePedidoRepository.findByPedidoId(pedidoId);
        List<DetallePedidoDTO> detallesPedidoDTO = new ArrayList<>();
        for (DetallePedido detallePedido : detallesPedido) {
            DetallePedidoDTO detallePedidoDTO = toDto(detallePedido);
            detallesPedidoDTO.add(detallePedidoDTO);
        }
        return detallesPedidoDTO;
    }

    private DetallePedido toEntity(DetallePedidoDTO detallePedidoDTO) {
        return DetallePedido.builder()
                .cantidad(detallePedidoDTO.getCantidad())
                .subTotal(detallePedidoDTO.getSubTotal())
                .pedido(pedidoRepository.findById(detallePedidoDTO.getPedido().getId())
                        .orElseThrow(() -> new RuntimeException("Pedido no encontrado")))
                .producto(productoRepository.findById(detallePedidoDTO.getProducto().getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado")))
                .insumo(insumoRepository.findById(detallePedidoDTO.getInsumo().getId())
                        .orElseThrow(() -> new RuntimeException("Insumo no encontrado")))
                .build();
    }
    public DetallePedidoDTO toDto(DetallePedido detallePedido) {
        return DetallePedidoDTO.builder()
                .id(detallePedido.getId())
                .cantidad(detallePedido.getCantidad())
                .subTotal(detallePedido.getSubTotal())
                .pedido(pedidoService.toDTO(detallePedido.getPedido()))
                .producto(productoService.toDTO(detallePedido.getProducto()))
                .insumo(insumoService.toDTO(detallePedido.getInsumo()))
                .build();
    }
}
