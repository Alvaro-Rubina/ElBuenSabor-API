package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.ProductoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public ProductoService(GenericoRepository<Producto, Long> genericoRepository, GenericoMapper<Producto, ProductoDTO, ProductoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public Producto save(ProductoDTO productoDTO) {
        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubroId())
                .orElseThrow(() -> new IllegalArgumentException("RubroProducto con el id " + productoDTO.getRubroId() + " no encontrado")));

        // Manejo de detalles
        producto.setDetalleProductos(new ArrayList<>());
        for (DetalleProductoDTO detalleDTO : productoDTO.getDetalleProductos()) {
            DetalleProducto detalle = detalleProductoService.createDetalleProducto(detalleDTO);
            detalle.setProducto(producto);
            producto.getDetalleProductos().add(detalle);
        }
        producto.setPrecioCosto(getPrecioCosto(producto.getDetalleProductos()));

        return (productoRepository.save(producto));
    }

    // Acá busca por denominacion parcial. Ej para "Pizza Margarita" busca Pizza o margarita, etc
    public List<ProductoResponseDTO> getProductosByDenominacion(String denominacion) {
        return productoRepository.findByDenominacionContainingIgnoreCase(denominacion).stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public void update(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto con el id " + id + " no encontrado"));

        // Actualizar campos básicos
        producto.setDenominacion(productoDTO.getDenominacion());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setTiempoEstimadoPreparacion(productoDTO.getTiempoEstimadoPreparacion());
        producto.setPrecioVenta(productoDTO.getPrecioVenta());
        producto.setUrlImagen(productoDTO.getUrlImagen());
        producto.setActivo(productoDTO.getActivo());

        // Actualizar rubro
        producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubroId())
                .orElseThrow(() -> new IllegalArgumentException("RubroProducto con el id " + productoDTO.getRubroId() + " no encontrado")));

        // Actualizar detalles
        producto.getDetalleProductos().clear();
        for (DetalleProductoDTO detalleDTO : productoDTO.getDetalleProductos()) {
            DetalleProducto detalle = detalleProductoService.createDetalleProducto(detalleDTO);
            detalle.setProducto(producto);
            producto.getDetalleProductos().add(detalle);
        }
        producto.setPrecioCosto(getPrecioCosto(producto.getDetalleProductos()));

        productoRepository.save(producto);
    }

    public void delete(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    // Metodos auxiliares
    private Double getPrecioCosto(List<DetalleProducto> detalleProductos) {
        Double precioCosto = 0.0;

        for (DetalleProducto detalleProducto : detalleProductos) {
            precioCosto += detalleProducto.getCantidad() * detalleProducto.getInsumo().getPrecioCosto();
        }
        return precioCosto;
    }

    public void actualizarEstadoProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto con el id " + id + " no encontrado"));
        if (producto.getActivo()) {
            producto.setActivo(false);
        } else {
            producto.setActivo(true);
        }
        productoRepository.save(producto);
    }
}
