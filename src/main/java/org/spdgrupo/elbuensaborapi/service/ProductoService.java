package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
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
    private final InsumoRepository insumoRepository;

    @Transactional
    public void saveProducto(ProductoDTO productoDTO) {
        Producto producto = toEntity(productoDTO);
        Producto productoGuardado = productoRepository.save(producto);

        // Una vez guardado el producto, se guardan los detalleProductos y se calcula el precioCosto
        detalleProductoService.saveMultipleDetalleProducto(productoDTO.getDetalleProductos(), productoGuardado.getId());

        producto.setPrecioCosto(calcularPrecioCosto(productoDTO.getDetalleProductos()));
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
    public void updateProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con el id " + id + " no encontrado"));

        if (!producto.getDenominacion().equals(productoDTO.getDenominacion())) {
            producto.setDenominacion(productoDTO.getDenominacion());
        }

        if (!producto.getDescripcion().equals(productoDTO.getDescripcion())) {
            producto.setDescripcion(productoDTO.getDescripcion());
        }

        if (!producto.getTiempoEstimadoPreparacion().equals(productoDTO.getTiempoEstimadoPreparacion())) {
            producto.setTiempoEstimadoPreparacion(productoDTO.getTiempoEstimadoPreparacion());
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

        if (!producto.getRubro().getId().equals(productoDTO.getRubroId())) {
            producto.setRubro(rubroProductoRepository.findById(productoDTO.getRubroId())
                    .orElseThrow(() -> new RuntimeException("RubroProducto con el id" + productoDTO.getRubroId() + " no encontrado")));
        }

        // TODO: SOLO FALTA LA LOGICA PARA ACTUALIZAR DETALLEPRODUCTOS; ACA O EN DETALLEPRODUCTOSERVICE

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
                .precioCosto(0.0) // Precio temporal, esto se calcula una vez se guarden los detalleProductos
                .urlImagen(productoDTO.getUrlImagen())
                .activo(productoDTO.isActivo())
                .rubro(rubroProductoRepository.findById(productoDTO.getRubroId())
                        .orElseThrow(() -> new RuntimeException("RubroProducto con el id " + productoDTO.getRubroId() + " no encontrado")))
                .build();
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
                .activo(producto.isActivo())
                .rubro(rubroProductoService.toDTO(producto.getRubro()))
                .detalleProductos(producto.getDetalleProductos().stream()
                        .map(detalleProductoService::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // METODOS AUXILIARES
    private double calcularPrecioCosto(List<DetalleProductoDTO> detalles) {
        return detalles.stream()
                .mapToDouble(detalle -> {
                    Insumo insumo = insumoRepository.findById(detalle.getInsumoId())
                            .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));
                    return insumo.getPrecioCompra() * detalle.getCantidad();
                })
                .sum();
    }
}
