package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private RubroProductoService rubroProductoService;

    @Autowired
    private RubroProductoRepository rubroProductoRepository;

    public void saveProducto(ProductoDTO productoDTO) {
        Producto producto = toEntity(productoDTO);
        productoRepository.save(producto);
    }

    public ProductoDTO getProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));
        return toDTO(producto);
    }

    public List<ProductoDTO> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        List<ProductoDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            productosDTO.add(toDTO(producto));
        }
        return productosDTO;
    }

    public void editProducto(Long id, ProductoDTO productoDTO) {
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

        if (!producto.isActivo() == productoDTO.isActivo()) {
            producto.setActivo(productoDTO.isActivo());
        }

        if (!producto.getRubro().getId().equals(productoDTO.getRubro().getId())) {
            producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubro().getId())
                    .orElseThrow(() -> new RuntimeException("RubroProducto con el id" + productoDTO.getRubro().getId() + " no encontrado")));
        }

        productoRepository.save(producto);
    }

    // MAPPERS
    public Producto toEntity(ProductoDTO productoDTO) {
        return Producto.builder()
                .denominacion(productoDTO.getDenominacion())
                .descripcion(productoDTO.getDescripcion())
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
