package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.enums.UnidadMedida;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.ProductoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;

import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;

import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductoService extends GenericoServiceImpl<Producto, ProductoDTO, ProductoResponseDTO, Long> {

    //TODO: marcar productos que no tienen stock de insumos como “no disponibles”.

    // Dependencias
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private RubroProductoRepository rubroProductoRepository;
    @Autowired
    private DetalleProductoService detalleProductoService;
    @Autowired
    private ProductoMapper productoMapper;
    @Autowired
    private PromocionService promocionService;


    public ProductoService(GenericoRepository<Producto, Long> genericoRepository, GenericoMapper<Producto, ProductoDTO, ProductoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public ProductoResponseDTO save(ProductoDTO productoDTO) {
        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubroId())
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + productoDTO.getRubroId() + " no encontrado")));

        // Manejo de detalles
        producto.setDetalleProductos(new ArrayList<>());
        for (DetalleProductoDTO detalleDTO : productoDTO.getDetalleProductos()) {
            DetalleProducto detalle = detalleProductoService.createDetalleProducto(detalleDTO);
            detalle.setProducto(producto);
            producto.getDetalleProductos().add(detalle);
        }
        producto.setPrecioCosto(getPrecioCosto(producto.getDetalleProductos()));
        producto.setPrecioVenta(getPrecioVenta(producto.getPrecioCosto(), productoDTO.getMargenGanancia()));

        productoRepository.save(producto);
        return productoMapper.toResponseDTO(producto);
    }

    public ProductoResponseDTO findByDenominacion(String denominacion) {
        Producto producto = productoRepository.findByDenominacion(denominacion)
                .orElseThrow(() -> new NotFoundException("Producto con la denominacion " + denominacion + " no encontrado"));
        return productoMapper.toResponseDTO(producto);
    }

    // Acá busca por denominacion parcial. Ej para "Pizza Margarita" busca Pizza o margarita, etc
    public List<ProductoResponseDTO> findByDenominacionContaining(String denominacion) {
        return productoRepository.findByDenominacionContainingIgnoreCase(denominacion).stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    public List<ProductoResponseDTO> findProductosByRubroId(Long rubroProductoId) {
        return productoRepository.findByRubroId(rubroProductoId).stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void cambiarActivoProducto(long id, boolean activo) {
        List<Producto> productos = productoRepository.findByDetalleProductosInsumoId(id);

        for (Producto producto : productos) {
            if (producto.getActivo() != activo) {
                if (activo) {
                    // Solo activa si todos los insumos están activos
                    boolean todosInsumosActivos = producto.getDetalleProductos().stream()
                            .allMatch(detalle -> detalle.getInsumo().getActivo());
                    if (todosInsumosActivos) {
                        activate(producto.getId());
                        promocionService.cambiarEstadoPromocionesPorProducto(producto.getId(), true);
                    }
                } else {
                    // Desactiva el producto y sus promociones
                    delete(producto.getId());
                    promocionService.cambiarEstadoPromocionesPorProducto(producto.getId(), false);
                }
            }
        }
    }

    @Override
    @Transactional
    public ProductoResponseDTO update(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto con el id " + id + " no encontrado"));

        if (!producto.getDenominacion().equals(productoDTO.getDenominacion())) {
            producto.setDenominacion(productoDTO.getDenominacion());
        }

        if (!producto.getDescripcion().equals(productoDTO.getDescripcion())) {
            producto.setDescripcion(productoDTO.getDescripcion());
        }

        if (!producto.getTiempoEstimadoPreparacion().equals(productoDTO.getTiempoEstimadoPreparacion())) {
            producto.setTiempoEstimadoPreparacion(productoDTO.getTiempoEstimadoPreparacion());
        }

        /*if (!producto.getPrecioVenta().equals(productoDTO.getPrecioVenta())) {
            producto.setPrecioVenta(productoDTO.getPrecioVenta());
        }*/

        if (!producto.getUrlImagen().equals(productoDTO.getUrlImagen())) {
            producto.setUrlImagen(productoDTO.getUrlImagen());
        }

        if (!Objects.equals(producto.getActivo(), productoDTO.getActivo())) {
            producto.setActivo(productoDTO.getActivo());
        }

        if (!producto.getRubro().getId().equals(productoDTO.getRubroId())) {
            producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubroId())
                    .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + productoDTO.getRubroId() + " no encontrado")));
        }

        producto.getDetalleProductos().clear();
        for (DetalleProductoDTO detalleDTO : productoDTO.getDetalleProductos()) {
            DetalleProducto detalle = detalleProductoService.createDetalleProducto(detalleDTO);
            detalle.setProducto(producto);
            producto.getDetalleProductos().add(detalle);
        }
        producto.setPrecioCosto(getPrecioCosto(producto.getDetalleProductos()));

        if (!producto.getPrecioVenta().equals(getPrecioVenta(producto.getPrecioCosto(), productoDTO.getMargenGanancia()))) {
            producto.setPrecioVenta(getPrecioVenta(producto.getPrecioCosto(), productoDTO.getMargenGanancia()));
            producto.setMargenGanancia(productoDTO.getMargenGanancia());
        }

        return productoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto con el id " + id + " no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Transactional
    public void activate(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        producto.setActivo(true);
        productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoVentasDTO> obtenerProductosMasVendidos(LocalDate fechaDesde, LocalDate fechaHasta, int limite) {
        // Validación de fechas
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("La fechaDesde no puede ser posterior a la fechaHasta.");
        }

        // Consulta al repositorio
        return productoRepository.findProductosMasVendidos(fechaDesde, fechaHasta, PageRequest.of(0, limite));
    }

    @Override
    @Transactional
    public String toggleActivo(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto con el id " + id + " no encontrado"));

        Boolean valorAnterior = producto.getActivo();
        Boolean activar = !producto.getActivo();

        if (activar) {

            if (!producto.getRubro().getActivo()) {
                throw new RuntimeException("No es posible activar el producto porque su rubro (" + producto.getRubro().getDenominacion() + ") está inactivo");
            }

            boolean todosInsumosActivos = producto.getDetalleProductos().stream()
                    .allMatch(detalle -> detalle.getInsumo().getActivo());
            if (!todosInsumosActivos) {
                throw new RuntimeException("No se puede activar el producto porque tiene insumos desactivados.");
            }
        }

        producto.setActivo(activar);
        productoRepository.save(producto);

        if (!activar) {
            promocionService.cambiarEstadoPromocionesPorProducto(producto.getId(),false);
        }else{
            promocionService.cambiarEstadoPromocionesPorProducto(producto.getId(),true);
        }

        Boolean valorActualizado = producto.getActivo();
        return "Estado 'activo' actualizado" +
                "\n- Valor anterior: " + valorAnterior +
                "\n- Valor actualizado: " + valorActualizado;

    }


    // Metodos auxiliares
    private Double getPrecioCosto(List<DetalleProducto> detalleProductos) {
        Double precioCosto = 0.0;

        for (DetalleProducto detalleProducto : detalleProductos) {
            Double cantidad = detalleProducto.getCantidad();
            UnidadMedida unidad = detalleProducto.getInsumo().getUnidadMedida();

            if (unidad == UnidadMedida.GRAMOS || unidad == UnidadMedida.MILILITROS) {
                cantidad /= 100.0;
            }

            precioCosto += cantidad * detalleProducto.getInsumo().getPrecioCosto();
        }
        return precioCosto;
    }

    private Double getPrecioVenta(Double precioCosto, Double margenGanancia) {
        double margen = margenGanancia / 100.0;
        return precioCosto * (1 + margen);
    }

    // ProductoService.java

    public List<Producto> obtenerProductosPorInsumo(Long insumoId) {
        return productoRepository.findByDetalleProductosInsumoId(insumoId);
    }

    @Transactional
    public void actualizarPreciosProducto(Producto producto) {
        Double nuevoPrecioCosto = getPrecioCosto(producto.getDetalleProductos());
        Double nuevoPrecioVenta = getPrecioVenta(nuevoPrecioCosto, producto.getMargenGanancia());
        producto.setPrecioCosto(nuevoPrecioCosto);
        producto.setPrecioVenta(nuevoPrecioVenta);
        productoRepository.save(producto);
    }

}
