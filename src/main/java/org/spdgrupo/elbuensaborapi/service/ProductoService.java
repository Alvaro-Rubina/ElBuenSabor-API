package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.mappers.ProductoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    //TODO: marcar productos que no tienen stock de insumos como “no disponibles”.

    // Dependencias
    private final ProductoRepository productoRepository;
    private final RubroProductoRepository rubroProductoRepository;
    private final DetalleProductoService detalleProductoService;
    private final ProductoMapper productoMapper;

    @Transactional
    public void saveProducto(ProductoDTO productoDTO) {
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

        productoRepository.save(producto);
    }

    public ProductoResponseDTO getProductoById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));
        return productoMapper.toResponseDTO(producto);
    }

    // Acá busca por denominacion parcial. Ej para "Pizza Margarita" busca Pizza o margarita, etc
    public List<ProductoResponseDTO> getProductosByDenominacion(String denominacion) {
        return productoRepository.findByDenominacionContainingIgnoreCase(denominacion).stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    // Acá busca por rubro, y si no se le pasa parametro (o es 0), busca todos
    public List<ProductoResponseDTO> getAllProductos(Long rubroId) {
        List<Producto> productos = (rubroId == null || rubroId == 0L)
                ? productoRepository.findAll()
                : productoRepository.findByRubroId(rubroId);

        return productos.stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void updateProducto(Long id, ProductoPatchDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto con el id " + id + " no encontrado"));

        // Actualizar campos básicos
        updateProductoFields(producto, productoDTO);

        // Actualizar rubro si es necesario
        if (productoDTO.getRubroId() != null && !producto.getRubro().getId().equals(productoDTO.getRubroId())) {
            producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubroId())
                    .orElseThrow(() -> new IllegalArgumentException("RubroProducto con el id " + productoDTO.getRubroId() + " no encontrado")));
        }

        // Actualizar detalles si es necesario
        if (productoDTO.getDetalleProductos() != null) {
            updateDetalleProductos(producto, productoDTO.getDetalleProductos());
        }

        productoRepository.save(producto);
    }

    public void deleteProducto(Long id) {
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


    private void updateProductoFields(Producto producto, ProductoPatchDTO productoDTO) {
        if (productoDTO.getDenominacion() != null) producto.setDenominacion(productoDTO.getDenominacion());
        if (productoDTO.getDescripcion() != null) producto.setDescripcion(productoDTO.getDescripcion());
        if (productoDTO.getTiempoEstimadoPreparacion() != null) producto.setTiempoEstimadoPreparacion(productoDTO.getTiempoEstimadoPreparacion());
        if (productoDTO.getPrecioVenta() != null) producto.setPrecioVenta(productoDTO.getPrecioVenta());
        if (productoDTO.getUrlImagen() != null) producto.setUrlImagen(productoDTO.getUrlImagen());
        if (productoDTO.getActivo() != null) producto.setActivo(productoDTO.getActivo());
    }

    private void updateDetalleProductos(Producto producto, List<DetalleProductoDTO> detallesDTO) {
        producto.getDetalleProductos().clear();
        for (DetalleProductoDTO detalleDTO : detallesDTO) {
            DetalleProducto detalle = detalleProductoService.createDetalleProducto(detalleDTO);
            detalle.setProducto(producto);
            producto.getDetalleProductos().add(detalle);
        }
        producto.setPrecioCosto(getPrecioCosto(producto.getDetalleProductos()));
    }
}
