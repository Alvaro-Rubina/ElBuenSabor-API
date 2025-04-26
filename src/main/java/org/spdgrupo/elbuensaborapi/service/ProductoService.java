package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    //TODO :marcar productos que no tienen stock de insumos como “no disponibles”.
    // Dependencias
    private final ProductoRepository productoRepository;
    private final RubroProductoService rubroProductoService;

    @Autowired
    private RubroProductoRepository rubroProductoRepository;

    public void saveProducto(ProductoDTO productoDTO) {
        Producto producto = toEntity(productoDTO);
        productoRepository.save(producto);
    }

    public ProductoDTO getProductoById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));
        return toDTO(producto);
    }

    // Acá busca por denominacion parcial. Ej para "Pizza Margarita" busca Pizza o margarita, etc
    public List<ProductoDTO> getProductosByDenominacion(String denominacion) {
        List<Producto> productos = productoRepository.findByDenominacionContainingIgnoreCase(denominacion);
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            productosDTO.add(toDTO(producto));
        }
        return productosDTO;
    }

    // Acá busca por rubro parcial. Ej para "Pizza" busca Pizza o izza, etc
    public List<ProductoDTO> getProductosByDenominacionCategoria(String categoria) {
        List<Producto> productos = productoRepository.findByRubroDenominacionContainingIgnoreCase(categoria);
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            productosDTO.add(toDTO(producto));
        }
        return productosDTO;
    }

    // Acá busca por id del rubro
    public List<ProductoDTO> getProductosByCategoriaId(Long categoriaId) {
        List<Producto> productos = productoRepository.findByRubroId(categoriaId);
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            productosDTO.add(toDTO(producto));
        }
        return productosDTO;
    }

    public List<ProductoDTO> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            productosDTO.add(toDTO(producto));
        }
        return productosDTO;
    }

    public void updateProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));

        if (!producto.getDenominacion().equals(productoDTO.getDenominacion())) {
            producto.setDenominacion(productoDTO.getDenominacion());
        }

        if (!producto.getDescripcion().equals(productoDTO.getDescripcion())) {
            producto.setDescripcion(productoDTO.getDescripcion());
        }

        if (!producto.getPrecioVenta().equals(productoDTO.getPrecioVenta())) {
            producto.setPrecioVenta(productoDTO.getPrecioVenta());
        }

        if (!producto.getUrlImagen().equals(productoDTO.getUrlImagen())) {
            producto.setUrlImagen(productoDTO.getUrlImagen());
        }

        if (!producto.getTiempoEstimadoPreparacion().equals(productoDTO.getTiempoEstimadoPreparacion())) {
            producto.setTiempoEstimadoPreparacion(productoDTO.getTiempoEstimadoPreparacion());
        }

        if (!producto.isActivo() == productoDTO.isActivo()) {
            producto.setActivo(productoDTO.isActivo());
        }

        if (!producto.getRubro().getId().equals(productoDTO.getRubro().getId())) {
            producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubro().getId())
                    .orElseThrow(() -> new RuntimeException("RubroProducto con el id" + productoDTO.getRubro().getId() + " no encontrado")));
        }

        productoRepository.save(producto);
    }

    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    // MAPPERS
    public Producto toEntity(ProductoDTO productoDTO) {
        return Producto.builder()
                .denominacion(productoDTO.getDenominacion())
                .descripcion(productoDTO.getDescripcion())
                .tiempoEstimadoPreparacion(productoDTO.getTiempoEstimadoPreparacion())
                .precioVenta(productoDTO.getPrecioVenta())
                .urlImagen(productoDTO.getUrlImagen())
                .activo(productoDTO.isActivo())
                .rubro(rubroProductoRepository.findById(productoDTO.getRubro().getId())
                        .orElseThrow(() -> new RuntimeException("RubroProducto")))
                .build();
    }

    public ProductoDTO toDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .denominacion(producto.getDenominacion())
                .descripcion(producto.getDescripcion())
                .precioVenta(producto.getPrecioVenta())
                .urlImagen(producto.getUrlImagen())
                .activo(producto.isActivo())
                .rubro(rubroProductoService.toDTO(producto.getRubro()))
                .build();
    }
}
