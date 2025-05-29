package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.PedidoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.*;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService extends GenericoServiceImpl<Pedido, PedidoDTO, PedidoResponseDTO, Long>{

    // Dependencias
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private DetallePedidoService detallePedidoService;
    @Autowired
    private FacturaService facturaService;
    @Autowired
    private PedidoMapper pedidoMapper;
    @Autowired
    private InsumoService insumoService;

    public PedidoService(GenericoRepository<Pedido, Long> genericoRepository, GenericoMapper<Pedido, PedidoDTO, PedidoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Transactional
    public void savePedido(PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);

        // Establecer cliente y domicilio
        pedido.setCliente(clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + pedidoDTO.getClienteId() + " no encontrado")));
        pedido.setDomicilio(domicilioRepository.findById(pedidoDTO.getDomicilioId())
                .orElseThrow(() -> new NotFoundException("Domicilio con el id " + pedidoDTO.getDomicilioId() + " no encontrado")));

        // manejo de detalles
        pedido.setDetallePedidos(new ArrayList<>());
        for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetallePedidos()) {
            DetallePedido detalle = detallePedidoService.createDetallePedido(detalleDTO);
            detalle.setPedido(pedido);
            pedido.getDetallePedidos().add(detalle);
        }

        // calculo totales y hora estimada
        pedido.setTotalVenta(getTotalVenta(pedido.getDetallePedidos()));
        pedido.setTotalCosto(getTotalCosto(pedido.getDetallePedidos()));
        pedido.setHoraEstimadaFin(getHoraEstimadaFin(pedido));
        pedido.setCodigo(generateCodigo());

        // Acá se descuentan los insumos antes de guardar el pedido
        for (DetallePedido detallePedido : pedido.getDetallePedidos()) {
            if (detallePedido.getProducto() != null) {
                Producto producto = detallePedido.getProducto();
                List<DetalleProducto> detallesProducto = producto.getDetalleProductos();
                for (DetalleProducto detalleProducto : detallesProducto) {
                    Insumo insumo = detalleProducto.getInsumo();
                    double cantidaADescontar = detalleProducto.getCantidad() * detallePedido.getCantidad();
                    insumoService.actualizarStock(insumo.getId(), -cantidaADescontar);
                }
            } else if (detallePedido.getInsumo() != null) {
                Insumo insumo = detallePedido.getInsumo();
                double cantidadADescontar = detallePedido.getCantidad();
                insumoService.actualizarStock(insumo.getId(), -cantidadADescontar);
            }
        }

        pedidoRepository.save(pedido);
    }

    public PedidoResponseDTO getPedidoByCodigo(String codigo) {
        Pedido pedido = pedidoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NotFoundException("Pedido con el código de orden " + codigo + " no encontrado"));
        return pedidoMapper.toResponseDTO(pedido);
    }

    public List<PedidoResponseDTO> getAllPedidos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void actualizarEstadoDelPedido(Long pedidoId, Estado estado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + pedidoId + " no encontrado"));

        if (estado != null) {
            pedido.setEstado(estado);

            if (estado == Estado.SOLICITADO) {
                facturaService.createFacturaFromPedido(pedido);
            }
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

    private String generateCodigo() {
        LocalDate hoy = LocalDate.now();
        int anio = hoy.getYear();
        int mes = hoy.getMonthValue();

        int pedidosEsteMes = pedidoRepository.countByYearAndMonth(anio, mes);
        int nuevoNumero = pedidosEsteMes + 1;

        String anioStr = String.valueOf(anio).substring(2);
        String mesStr = String.format("%02d", mes);
        String numeroStr = String.format("%05d", nuevoNumero);

        return "PED-" + anioStr + mesStr + "-" + numeroStr;
    }
}
