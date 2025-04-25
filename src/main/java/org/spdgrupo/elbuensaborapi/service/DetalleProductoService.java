package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.repository.DetalleProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleProductoService {

    // Dependencias
    private final DetalleProductoRepository detalleProductoRepository;
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    public void saveDetalleProducto(DetalleProductoDTO detalleProductoDTO) {
        DetalleProducto detalleProducto = toEntity(detalleProductoDTO);
        detalleProductoRepository.save(detalleProducto);
    }

    public DetalleProductoDTO getDetalleProductoById(Long id) {
        DetalleProducto detalleProducto = detalleProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de producto con el id " + id + " no encontrado"));
        return toDTO(detalleProducto);
    }

    public List<DetalleProductoDTO> getAllDetallesProducto() {
        List<DetalleProducto> detallesProducto = detalleProductoRepository.findAll();
        List<DetalleProductoDTO> detallesProductoDTO = new ArrayList<>();

        for (DetalleProducto detalleProducto : detallesProducto) {
            detallesProductoDTO.add(toDTO(detalleProducto));
        }
        return detallesProductoDTO;
    }

    public void updateDetalleProducto(Long id, DetalleProductoDTO detalleProductoDTO) {
        DetalleProducto detalleProducto = detalleProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de producto con el id " + id + " no encontrado"));

        if (!detalleProducto.getCantidad().equals(detalleProductoDTO.getCantidad())) {
            detalleProducto.setCantidad(detalleProductoDTO.getCantidad());
        }

        if (!detalleProducto.getProducto().getId().equals(detalleProductoDTO.getProducto().getId())) {
            detalleProducto.setProducto(productoRepository.findById(detalleProductoDTO.getProducto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto con id " + detalleProductoDTO.getProducto().getId() + " no encontrado")));
        }

        if (!detalleProducto.getInsumo().getId().equals(detalleProductoDTO.getInsumo().getId())) {
            detalleProducto.setInsumo(insumoRepository.findById(detalleProductoDTO.getInsumo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Insumo con id " + detalleProductoDTO.getInsumo().getId() + " no encontrado")));
        }
        
        detalleProductoRepository.save(detalleProducto);
    }

    // MAPPERS
    private DetalleProducto toEntity(DetalleProductoDTO detalleProductoDTO) {
        return DetalleProducto.builder()
                .cantidad(detalleProductoDTO.getCantidad())
                .producto(productoRepository.findById(detalleProductoDTO.getProducto().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto con id " + detalleProductoDTO.getProducto().getId() + " no encontrado")))
                .insumo(insumoRepository.findById(detalleProductoDTO.getInsumo().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Insumo con id " + detalleProductoDTO.getInsumo().getId() + " no encontrado")))
                .build();
    }

    public DetalleProductoDTO toDTO(DetalleProducto detalleProducto) {
        return DetalleProductoDTO.builder()
                .id(detalleProducto.getId())
                .cantidad(detalleProducto.getCantidad())
                .producto(productoService.toDTO(detalleProducto.getProducto()))
                .insumo(insumoService.toDTO(detalleProducto.getInsumo()))
                .build();
    }
}
