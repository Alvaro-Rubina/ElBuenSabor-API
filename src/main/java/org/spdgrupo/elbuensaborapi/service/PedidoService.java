package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    // Dependencias
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final DomicilioRepository domicilioRepository;
    private final DomicilioService domicilioService;
    private final ClienteService clienteService;
    private final DetallePedidoService detallePedidoService;

    public void savePedido(PedidoDTO pedidoDTO) {
        Pedido pedido = toEntity(pedidoDTO);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Una vez guardado el pedido, se guardan los detallePedidos y se calcula el precioCosto
        detallePedidoService.saveDetallesPedidos(pedidoDTO.getDetallePedidos(), pedidoGuardado);

        pedido.setHoraEstimadaFin(calculateTiempoEstimadoPreparacion(pedidoGuardado));
        pedidoGuardado.setTotalVenta(calculateTotalVenta(pedidoGuardado));
        pedidoGuardado.setTotalCosto(calculateTotalCosto(pedidoGuardado));
        pedidoRepository.save(pedidoGuardado);
    }

    public PedidoResponseDTO getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + id + " no encontrado"));
        return toDTO(pedido);
    }

    public PedidoResponseDTO getPedidoByCodigo(String codigoOrden) {
        Pedido pedido = pedidoRepository.findByCodigoOrden(codigoOrden)
                .orElseThrow(() -> new NotFoundException("Pedido con el código de orden " + codigoOrden + " no encontrado"));
        return toDTO(pedido);
    }

    public List<PedidoResponseDTO> getAllPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<PedidoResponseDTO> pedidosDTO = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            PedidoResponseDTO pedidoDTO = toDTO(pedido);
            pedidosDTO.add(pedidoDTO);
        }
        return pedidosDTO;
    }

    public void updatePedido(Long id, PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + id + " no encontrado"));

        pedidoRepository.save(pedido);
    }

    public void agregarTiempoAlPedido(Long pedidoId, Long minutos) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + pedidoId + " no encontrado"));

        pedido.setHoraEstimadaFin(pedido.getHoraEstimadaFin().plusMinutes(minutos));
    }

    // MAPPERS
    public Pedido toEntity(PedidoDTO pedidoDTO) {

        return Pedido.builder()
                .fecha(LocalDate.now())
                .hora(LocalTime.now())
                .codigoOrden(null) // TODO: ACA VER LO DEL TEMA DEL CODIGO
                .estado(Estado.SOLICITADO) // Estado por defecto al crear un nuevo pedido.
                .horaEstimadaFin(null) // TODO: ACA VER LO DEL TEMA HORA ESTIMADA FIN
                .tipoEnvio(pedidoDTO.getTipoEnvio())
                .totalVenta(null)
                .totalCosto(null) // TODO: ACA VER LO DEL TOTAL VENTA
                .formaPago(pedidoDTO.getFormaPago())
                .cliente(clienteRepository.findById(pedidoDTO.getClienteId())
                        .orElseThrow(() -> new NotFoundException("Cliente con el id " + pedidoDTO.getClienteId() + " no encontrado")))
                .domicilio(domicilioRepository.findById(pedidoDTO.getDomicilioId())
                        .orElseThrow(() -> new NotFoundException("Domicilio con el id " + pedidoDTO.getDomicilioId() + " no encontrado")))
                .build();
    }

    public PedidoResponseDTO toDTO(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .fecha(pedido.getFecha())
                .codigoOrden(pedido.getCodigoOrden())
                .estado(pedido.getEstado())
                .horaEstimadaFin(pedido.getHoraEstimadaFin())
                .tipoEnvio(pedido.getTipoEnvio())
                .totalVenta(pedido.getTotalVenta())
                .totalCosto(pedido.getTotalCosto())
                .formaPago(pedido.getFormaPago())
                .cliente(clienteService.toDTO(pedido.getCliente()))
                .domicilio(domicilioService.toDTO(pedido.getDomicilio()))
                .detallePedidos(pedido.getDetallePedidos().stream()
                        .map(detallePedidoService::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // Metodos adicionales
    public Double calculateTotalVenta(Pedido pedido) {
        return pedido.getDetallePedidos().stream()
                .mapToDouble(DetallePedido::getSubTotal)
                .sum();
    }

    public Double calculateTotalCosto(Pedido pedido) {
        return pedido.getDetallePedidos().stream()
                .mapToDouble(DetallePedido::getSubTotalCosto)
                .sum();
    }

    public LocalTime calculateTiempoEstimadoPreparacion(Pedido pedido) {
        List<DetallePedido> detallePedidos = pedido.getDetallePedidos();
        LocalTime mayorTiempo = LocalTime.MIN;

        for (DetallePedido detallePedido : detallePedidos) {
            Producto producto = detallePedido.getProducto();
            if (producto != null) {
                long tiempoEstimado = producto.getTiempoEstimadoPreparacion();

                if (detallePedido.getCantidad() == 2) {
                    tiempoEstimado = Math.round(tiempoEstimado * 1.25);
                } else if (detallePedido.getCantidad() >= 3) {
                    tiempoEstimado = Math.round(tiempoEstimado * 1.5);
                }

                // Convertimos minutos a segundos, luego a LocalTime
                LocalTime tiempoPreparacion = LocalTime.ofSecondOfDay(tiempoEstimado * 60);

                if (tiempoPreparacion.isAfter(mayorTiempo)) {
                    mayorTiempo = tiempoPreparacion;
                }
            }
        }

        // tiempo de prep = el mayor + 10 minutos
        return mayorTiempo.plusMinutes(10);
    }

}
