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
import org.spdgrupo.elbuensaborapi.service.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private EmailService emailService;

    public PedidoService(GenericoRepository<Pedido, Long> genericoRepository, GenericoMapper<Pedido, PedidoDTO, PedidoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @CacheEvict(value = "pedidos", allEntries = true)
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

        // Costo de envio segun tipo de envio TODO: El valor por ahora es provisional, después ajustar
        if (pedidoDTO.getTipoEnvio() == TipoEnvio.DELIVERY) {
            pedido.setCostoEnvio(2000.0);
        } else {
            pedido.setCostoEnvio(0.0);
        }

        // manejo de detalles
        pedido.setDetallePedidos(new ArrayList<>());
        for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetallePedidos()) {
            DetallePedido detalle = detallePedidoService.createDetallePedido(detalleDTO);
            detalle.setPedido(pedido);
            pedido.getDetallePedidos().add(detalle);
        }

        // calculo totales y hora estimada
        pedido.setTotalVenta(getTotalVenta(pedido.getDetallePedidos()) + pedido.getCostoEnvio());
        pedido.setTotalCosto(getTotalCosto(pedido.getDetallePedidos()));
        pedido.setHoraEstimadaFin(getHoraEstimadaFin(pedido));
        pedido.setCodigo(generateCodigo());

        // Acá se descuentan los insumos antes de guardar el pedido
        /*for (DetallePedido detallePedido : pedido.getDetallePedidos()) {
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
        }*/

        pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Cacheable(value = "pedidos", key = "'getPedidoByCodigo_'+#codigo")
    public PedidoResponseDTO getPedidoByCodigo(String codigo) {
        Pedido pedido = pedidoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NotFoundException("Pedido con el código de orden " + codigo + " no encontrado"));
        return pedidoMapper.toResponseDTO(pedido);
    }

    @CachePut(value = "pedidos", key = "'actualizarEstadoDelPedido_'+#pedidoId")
    @Transactional
    public PedidoResponseDTO actualizarEstadoDelPedido(Long pedidoId, Estado nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + pedidoId + " no encontrado"));

        Estado estadoActual = pedido.getEstado();

        if (nuevoEstado != null) {
            if (!estadoActual.puedeTransicionarA(nuevoEstado)) {
                throw new IllegalStateException("Transición de estado no permitida: de " + estadoActual + " a " + nuevoEstado);
            }

            pedido.setEstado(nuevoEstado);

            if (nuevoEstado == Estado.TERMINADO) {
                pedido.setFactura(facturaService.createFacturaFromPedido(pedido));

            }

            if (nuevoEstado == Estado.SOLICITADO ){
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
            }

            try {
                if (nuevoEstado == Estado.TERMINADO && pedido.getTipoEnvio() == TipoEnvio.RETIRO_LOCAL) {
                    enviarMailConFactura(pedido, "¡Tu pedido está listo para retirar!",
                            "¡Hola " +  pedido.getCliente().getNombreCompleto() + "! Tu pedido ya está disponible para retirar en el local. Adjuntamos la factura de compra.");
                }
                if (nuevoEstado == Estado.EN_CAMINO && pedido.getTipoEnvio() == TipoEnvio.DELIVERY) {
                    enviarMailConFactura(pedido, "¡Tu pedido está en camino!",
                            "¡Hola " + pedido.getCliente().getNombreCompleto() +"! Tu pedido ya está en camino. Adjuntamos la factura de compra.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }



        }

        pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(pedido);
    }

    // TODO: Agragar metodo para EMITIR UNA FACTURA
    @CachePut(value = "pedidos", key = "'agregarTiempoAlPedido_'+#pedidoId")
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

    @Cacheable(value = "pedidos", key = "'getPedidosByEstado_'+#estado")
    public List<PedidoResponseDTO> getPedidosByEstado(Estado estado) {
        List<Pedido> pedidos = pedidoRepository.findAllByEstado(estado);

        return pedidos.stream()
                .map(pedidoMapper::toResponseDTO)
                .toList();

    }

    @Cacheable(value = "pedidos", key = "'calcularIngresosEgresos_'+T(String).valueOf(#fechaDesde) + '-' + T(String).valueOf(#fechaHasta)")
    @Transactional(readOnly = true)
    public List<IngresosEgresosDTO> calcularIngresosEgresos(LocalDate fechaDesde, LocalDate fechaHasta) {

        List<Pedido> pedidos = pedidoRepository.findPedidosEntregados(fechaDesde, fechaHasta);

        if (fechaDesde == null && fechaHasta == null) {
            Map<LocalDate, IngresosEgresosDTO> mapaMeses = new HashMap<>();

            for (Pedido pedido : pedidos) {
                int anio = pedido.getFecha().getYear();
                int mes = pedido.getFecha().getMonthValue();
                LocalDate fechaMes = LocalDate.of(anio, mes, 1);

                IngresosEgresosDTO dtoMes = mapaMeses.get(fechaMes);
                if (dtoMes == null) {
                    dtoMes = IngresosEgresosDTO.builder()
                            .ingresos(pedido.getTotalVenta())
                            .egresos(pedido.getTotalCosto())
                            .ganancias(pedido.getTotalVenta() - pedido.getTotalCosto())
                            .fecha(fechaMes)
                            .build();
                    mapaMeses.put(fechaMes, dtoMes);
                } else {
                    dtoMes.setIngresos(dtoMes.getIngresos() + pedido.getTotalVenta());
                    dtoMes.setEgresos(dtoMes.getEgresos() + pedido.getTotalCosto());
                    dtoMes.setGanancias(dtoMes.getIngresos() - dtoMes.getEgresos());
                }
            }
            return new ArrayList<>(mapaMeses.values());
        }

        double ingresos = 0.0;
        double egresos = 0.0;

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == Estado.ENTREGADO) {
                ingresos += pedido.getTotalVenta();
                egresos += pedido.getTotalCosto();
            }
        }

        double ganancias = ingresos - egresos;

        return List.of(IngresosEgresosDTO.builder()
                .ingresos(ingresos)
                .egresos(egresos)
                .ganancias(ganancias)
                .fecha(null)
                .build());
    }

    @Cacheable(value = "pedidos", key = "'getPedidosByClienteId_'+#clienteId")
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> getPedidosByClienteId(Long clienteId, Estado estado) {
        List<Pedido> pedidos = pedidoRepository.findPedidosByClienteIdAndEstado(clienteId, estado);

        return pedidos.stream()
                .map(pedidoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Cacheable("pedidos")
    public List<PedidoResponseDTO> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "pedidos", key = "'findById_'+#id")
    public PedidoResponseDTO findById(Long id) {
        return super.findById(id);
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

    private void enviarMailConFactura(Pedido pedido, String asunto, String cuerpo) throws Exception {
        String email = pedido.getCliente().getUsuario().getEmail();
        byte[] facturaPdf = facturaService.exportarFacturaPdf(pedido.getId());
        emailService.enviarMailConAdjunto(email, asunto, cuerpo, facturaPdf, "factura-" + pedido.getCodigo() + ".pdf");
    }
}
