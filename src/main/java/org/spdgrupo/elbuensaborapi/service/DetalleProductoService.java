package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.repository.DetalleProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleProductoService {

    // Dependencias
    private final DetalleProductoRepository detalleProductoRepository;
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    @Transactional
    public void saveDetallesProductos(List<DetalleProductoDTO> detalleProductoDTOList, Producto producto) {
        for (DetalleProductoDTO detalleProductoDTO : detalleProductoDTOList) {
            DetalleProducto detalleProducto = toEntity(detalleProductoDTO, producto);
            detalleProductoRepository.save(detalleProducto);
        }
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
