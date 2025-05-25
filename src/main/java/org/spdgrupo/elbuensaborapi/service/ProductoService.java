package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final RubroProductoService rubroProductoService;
    private final RubroProductoRepository rubroProductoRepository;
    private final DetalleProductoService detalleProductoService;

    @Transactional
    public void saveProducto(ProductoDTO productoDTO) {
        Producto producto = toEntity(productoDTO);
        productoRepository.save(producto);
    }

    public ProductoResponseDTO getProductoById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));
        return toDTO(producto);
    }

    // Acá busca por denominacion parcial. Ej para "Pizza Margarita" busca Pizza o margarita, etc
    public List<ProductoResponseDTO> getProductosByDenominacion(String denominacion) {
        List<Producto> productos = productoRepository.findByDenominacionContainingIgnoreCase(denominacion);
        List<ProductoResponseDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            productosDTO.add(toDTO(producto));
        }
        return productosDTO;
    }

    // Acá busca por rubro, y si no se le pasa parametro (o es 0), busca todos
    public List<ProductoResponseDTO> getAllProductos(Long rubroId) {
        List<Producto> productos;
        if (rubroId == null || rubroId == 0L) {
            productos = productoRepository.findAll();
        } else {
            productos = productoRepository.findByRubroId(rubroId);
        }
        List<ProductoResponseDTO> productosDTO = new ArrayList<>();

        for (Producto producto : productos) {
            productosDTO.add(toDTO(producto));
        }
        return productosDTO;
    }

    @Transactional
    public void updateProducto(Long id, ProductoPatchDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));

        if (productoDTO.getDenominacion() != null) {
            producto.setDenominacion(productoDTO.getDenominacion());
        }

        if (productoDTO.getDescripcion() != null) {
            producto.setDescripcion(productoDTO.getDescripcion());
        }

        if (productoDTO.getTiempoEstimadoPreparacion() != null) {
            producto.setTiempoEstimadoPreparacion(productoDTO.getTiempoEstimadoPreparacion());
        }

        if (productoDTO.getPrecioVenta() != null) {
            producto.setPrecioVenta(productoDTO.getPrecioVenta());
        }

        if (productoDTO.getUrlImagen() != null) {
            producto.setUrlImagen(productoDTO.getUrlImagen());
        }

        if (productoDTO.getActivo() != null) {
            producto.setActivo(productoDTO.getActivo());
        }

        if (!producto.getRubro().getId().equals(productoDTO.getRubroId())) {
            producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubroId())
                    .orElseThrow(() -> new RuntimeException("RubroProducto con el id" + productoDTO.getRubroId() + " no encontrado")));
        }

        // acá tambien se podria verificar si la lista no viene vacia
        if (productoDTO.getDetalleProductos() != null) {
            producto.setDetalleProductos(new ArrayList<>());
            for (DetalleProductoDTO detalleProductoDTO : productoDTO.getDetalleProductos()) {
                DetalleProducto detalle = detalleProductoService.toEntity(detalleProductoDTO);
                detalle.setProducto(producto);
                producto.getDetalleProductos().add(detalle);
            }
            producto.setPrecioCosto(getPrecioCosto(producto.getDetalleProductos()));
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
        RubroProducto rubro = rubroProductoRepository.findById(productoDTO.getRubroId())
                .orElseThrow(() -> new RuntimeException("RubroProducto con el id " + productoDTO.getRubroId() + " no encontrado"));

        Producto producto = Producto.builder()
                .denominacion(productoDTO.getDenominacion())
                .descripcion(productoDTO.getDescripcion())
                .tiempoEstimadoPreparacion(productoDTO.getTiempoEstimadoPreparacion())
                .precioVenta(productoDTO.getPrecioVenta())
                .urlImagen(productoDTO.getUrlImagen())
                .activo(productoDTO.getActivo())
                .rubro(rubro)
                .detalleProductos(new ArrayList<>())
                .build();

        for (DetalleProductoDTO detalleProductoDTO : productoDTO.getDetalleProductos() ) {
            DetalleProducto detalle = detalleProductoService.toEntity(detalleProductoDTO);
            detalle.setProducto(producto);
            producto.getDetalleProductos().add(detalle);
        }
        producto.setPrecioCosto(getPrecioCosto(producto.getDetalleProductos()));

        return producto;
    }

    public ProductoResponseDTO toDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .denominacion(producto.getDenominacion())
                .descripcion(producto.getDescripcion())
                .tiempoEstimadoPreparacion(producto.getTiempoEstimadoPreparacion())
                .precioVenta(producto.getPrecioVenta())
                .precioCosto(producto.getPrecioCosto())
                .urlImagen(producto.getUrlImagen())
                .activo(producto.getActivo())
                /*.rubro(rubroProductoService.toDTO(producto.getRubro()))*/
                .detalleProductos(producto.getDetalleProductos().stream()
                        .map(detalleProductoService::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // Metodos adicionales
    private Double getPrecioCosto(List<DetalleProducto> detalleProductos) {
        Double precioCosto = 0.0;

        for (DetalleProducto detalleProducto : detalleProductos) {
            precioCosto += detalleProducto.getCantidad() * detalleProducto.getInsumo().getPrecioCosto();
        }
        return precioCosto;
    }
}
