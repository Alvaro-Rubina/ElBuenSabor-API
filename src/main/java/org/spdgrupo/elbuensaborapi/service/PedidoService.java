package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
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
        pedidoRepository.save(pedido);
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

        // TODO: en cada validacion verificar tambien que el valor no sea null o vacio
        if (pedido.getEstado() != pedidoDTO.getEstado()) {
            pedido.setEstado(pedidoDTO.getEstado());
        }

        if (pedido.getTipoEnvio() != pedidoDTO.getTipoEnvio()) {
            pedido.setTipoEnvio(pedidoDTO.getTipoEnvio());
        }

        if (pedido.getFormaPago() != pedidoDTO.getFormaPago()) {
            pedido.setFormaPago(pedidoDTO.getFormaPago());
        }

        if (pedido.getCliente().getId() != pedidoDTO.getClienteId()) {
            pedido.setCliente(clienteRepository.findById(pedidoDTO.getClienteId())
                    .orElseThrow(() -> new NotFoundException("Cliente con el id " + pedidoDTO.getClienteId() + " no encontrado")));
        }

        if (pedido.getDomicilio().getId() != pedidoDTO.getDomicilioId()) {
            pedido.setDomicilio(domicilioRepository.findById(pedidoDTO.getDomicilioId())
                    .orElseThrow(() -> new NotFoundException("Domicilio con el id " + pedidoDTO.getDomicilioId() + " no encontrado")));
        }

        pedidoRepository.save(pedido);
    }

    public void agregarTiempoAlPedido(Long pedidoId, Long minutos) {
        if (minutos == null || minutos < 0) {
            throw new IllegalArgumentException("El tiempo adicional debe ser un valor positivo.");
        }
    
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + pedidoId + " no encontrado"));

        pedido.setHoraEstimadaFin(pedido.getHoraEstimadaFin().plusMinutes(minutos));
        pedidoRepository.save(pedido);
    }

    // MAPPERS
    public Pedido toEntity(PedidoDTO pedidoDTO) {

        Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + pedidoDTO.getClienteId() + " no encontrado"));
        Domicilio domicilio = domicilioRepository.findById(pedidoDTO.getDomicilioId())
                .orElseThrow(() -> new NotFoundException("Domicilio con el id " + pedidoDTO.getDomicilioId() + " no encontrado"));

        Pedido pedido = Pedido.builder()
                .fecha(LocalDate.now())
                .hora(LocalTime.now())
                .codigoOrden(generateCodigoOrden())
                .estado(Estado.SOLICITADO) // Estado por defecto al crear un nuevo pedido.
                .tipoEnvio(pedidoDTO.getTipoEnvio())
                .formaPago(pedidoDTO.getFormaPago())
                .cliente(cliente)
                .domicilio(domicilio)
                .detallePedidos(new ArrayList<>())
                .build();

        for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetallePedidos()) {
            DetallePedido detalle = detallePedidoService.toEntity(detalleDTO);
            detalle.setPedido(pedido);
            pedido.getDetallePedidos().add(detalle);
        }
        pedido.setTotalVenta(getTotalVenta(pedido.getDetallePedidos()));
        pedido.setTotalCosto(getTotalCosto(pedido.getDetallePedidos()));
        pedido.setHoraEstimadaFin(getHoraEstimadaFin(pedido));

        return pedido;
    }

    public PedidoResponseDTO toDTO(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .fecha(pedido.getFecha())
                .hora(pedido.getHora())
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
    private Double getTotalVenta(List<DetallePedido> detallePedidos) {
        Double totalVenta = 0.0;

        for (DetallePedido detallePedido : detallePedidos) {
            totalVenta += detallePedido.getSubTotal();
        }
        return totalVenta;
    }

    private Double getTotalCosto(List<DetallePedido> detallePedidos) {
        Double totalCosto = 0.0;

        for (DetallePedido detallePedido : detallePedidos) {
            totalCosto += detallePedido.getSubTotalCosto();
        }

        return totalCosto;
    }

    private LocalTime getHoraEstimadaFin(Pedido pedido) {
        int maxTiempoPreparacion = 0;
        int cantidadProductos = 0;

        for (DetallePedido detalle : pedido.getDetallePedidos()) {
            if (detalle.getProducto() != null) {
                cantidadProductos++;
                Long tiempoPreparacion = detalle.getProducto().getTiempoEstimadoPreparacion();
                if (tiempoPreparacion != null && tiempoPreparacion > maxTiempoPreparacion) {
                    maxTiempoPreparacion = tiempoPreparacion.intValue();
                }
            }
        }

        // NOTE: Los tiempos adicionados pueden variar despues
        // el tiempo va a ser el max + 10 minutos
        int tiempoAdicional = maxTiempoPreparacion + 5;

        // si hay 3 o más productos, 5 minutitos mas
        if (cantidadProductos >= 3) {
            tiempoAdicional += 5;
        }

        return pedido.getHora().plusMinutes(tiempoAdicional);
    }

    private String generateCodigoOrden() {
        LocalDate hoy = LocalDate.now();
        int anio = hoy.getYear();     // 2025
        int mes = hoy.getMonthValue(); // 5

        int pedidosEsteMes = pedidoRepository.countByYearAndMonth(anio, mes);
        int nuevoNumero = pedidosEsteMes + 1;

        String anioStr = String.valueOf(anio).substring(2);       // "25"
        String mesStr = String.format("%02d", mes);               // "05"
        String numeroStr = String.format("%05d", nuevoNumero);    // "00042"

        return "PED-" + anioStr + mesStr + "-" + numeroStr;
    }



}
