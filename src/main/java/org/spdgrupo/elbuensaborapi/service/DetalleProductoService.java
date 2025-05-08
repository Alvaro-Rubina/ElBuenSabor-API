package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
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
    private final ProductoRepository productoRepository;
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    @Transactional
    public void saveDetallesProductos(List<DetalleProductoDTO> detalleProductoDTOList, Producto producto) {
        for (DetalleProductoDTO detalleProductoDTO : detalleProductoDTOList) {
            DetalleProducto detalleProducto = toEntity(detalleProductoDTO, producto);
            detalleProductoRepository.save(detalleProducto);
        }
    }

    public DetalleProductoResponseDTO getDetalleProductoById(Long id) {
        DetalleProducto detalleProducto = detalleProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de producto con el id " + id + " no encontrado"));
        return toDTO(detalleProducto);
    }

    public List<DetalleProductoResponseDTO> getAllDetallesProducto() {
        List<DetalleProducto> detallesProducto = detalleProductoRepository.findAll();
        List<DetalleProductoResponseDTO> detallesProductoDTO = new ArrayList<>();

        for (DetalleProducto detalleProducto : detallesProducto) {
            detallesProductoDTO.add(toDTO(detalleProducto));
        }
        return detallesProductoDTO;
    }

    // MAPPERS
    public DetalleProducto toEntity(DetalleProductoDTO detalleProductoDTO, Producto producto) {
        return DetalleProducto.builder()
                .cantidad(detalleProductoDTO.getCantidad())
                .producto(producto)
                .insumo(insumoRepository.findById(detalleProductoDTO.getInsumoId())
                        .orElseThrow(() -> new IllegalArgumentException("Insumo con el id " + detalleProductoDTO.getInsumoId() + " no encontrado")))
                .build();
    }

    public DetalleProductoResponseDTO toDTO(DetalleProducto detalleProducto) {
        return DetalleProductoResponseDTO.builder()
                .id(detalleProducto.getId())
                .cantidad(detalleProducto.getCantidad())
                .insumo(insumoService.toDTO(detalleProducto.getInsumo()))
                .build();
    }
}
