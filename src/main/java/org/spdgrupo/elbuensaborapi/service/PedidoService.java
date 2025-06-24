package org.spdgrupo.elbuensaborapi.service;


import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.PedidoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.IngresosEgresosDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.*;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.enums.FormaPago;
import org.spdgrupo.elbuensaborapi.model.enums.TipoEnvio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public PedidoResponseDTO save(PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);

        // cliente y domicilio
        pedido.setCliente(clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + pedidoDTO.getClienteId() + " no encontrado")));

        if (pedidoDTO.getDomicilioId() == null) {
            if (pedidoDTO.getTipoEnvio() == TipoEnvio.DELIVERY) throw new IllegalArgumentException("El domicilio es requerido para pedidos de tipo DELIVERY");

        } else {
            pedido.setDomicilio(domicilioRepository.findById(pedidoDTO.getDomicilioId())
                    .orElseThrow(() -> new NotFoundException("Domicilio con el id " + pedidoDTO.getDomicilioId() + " no encontrado")));
        }

        // Estado segun metodo de pago
        if (pedidoDTO.getFormaPago().equals(FormaPago.MERCADO_PAGO)) {
            pedido.setEstado(Estado.PENDIENTE_FACTURACION);
        } else {
            pedido.setEstado(Estado.SOLICITADO); // Por defecto, para pagos en efectivo
        }

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
        return pedidoMapper.toResponseDTO(pedido);
    }

    public PedidoResponseDTO getPedidoByCodigo(String codigo) {
        Pedido pedido = pedidoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NotFoundException("Pedido con el código de orden " + codigo + " no encontrado"));
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Transactional
    public PedidoResponseDTO actualizarEstadoDelPedido(Long pedidoId, Estado estado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + pedidoId + " no encontrado"));

        if (estado != null) {
            pedido.setEstado(estado);

            // NOTE: Esto creo que tendría que estar en el metodo savePedido
            if (estado == Estado.TERMINADO) {
                pedido.setFactura(facturaService.createFacturaFromPedido(pedido));
            }
        }

        pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(pedido);
    }

    // TODO: Agragar metodo para EMITIR UNA FACTURA

    public PedidoResponseDTO agregarTiempoAlPedido(Long pedidoId, Long minutos) {
        if (minutos == null || minutos < 0) {
            throw new IllegalArgumentException("El tiempo adicional debe ser un valor positivo.");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + pedidoId + " no encontrado"));

        pedido.setHoraEstimadaFin(pedido.getHoraEstimadaFin().plusMinutes(minutos));
        pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(pedido);
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
        // el tiempo va a ser el max + 5 minutos
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

    public List<PedidoResponseDTO> getPedidosByEstado(Estado estado) {
        List<Pedido> pedidos = pedidoRepository.findAllByEstado(estado);

        return pedidos.stream()
                .map(pedidoMapper::toResponseDTO)
                .toList();

    }

    @Transactional(readOnly = true)
    public IngresosEgresosDTO calcularIngresosEgresos(LocalDate fechaDesde, LocalDate fechaHasta) {
        List<Pedido> pedidos = pedidoRepository.findPedidosEntregadosYCancelados(fechaDesde, fechaHasta);

        if (pedidos.isEmpty()) {
            return new IngresosEgresosDTO(0.0, 0.0, 0.0);
        }

        double ingresos = 0.0;
        double egresos = 0.0;

        for (Pedido pedido : pedidos) {
            // Solo sumo el totalVenta si el pedido fue entregado, si fue cancelado no.
            if (pedido.getEstado() == Estado.ENTREGADO) {
                ingresos += pedido.getTotalVenta();
            }

            egresos += pedido.getTotalCosto();
        }

        double ganancias = ingresos - egresos;

        return new IngresosEgresosDTO(ingresos, egresos, ganancias);
    }
}
