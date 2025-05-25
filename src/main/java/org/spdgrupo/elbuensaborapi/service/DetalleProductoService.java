package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetalleProductoService {

    // Dependencias
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    // MAPPERS
    @Transactional
    public DetalleProducto toEntity(DetalleProductoDTO detalleProductoDTO) {
        Insumo insumo = insumoRepository.findById(detalleProductoDTO.getInsumoId())
                .orElseThrow(() -> new IllegalArgumentException("Insumo con el id " + detalleProductoDTO.getInsumoId() + " no encontrado"));
        return DetalleProducto.builder()
                .cantidad(detalleProductoDTO.getCantidad())
                .insumo(insumo)
                .build();
    }

    public DetalleProductoResponseDTO toDTO(DetalleProducto detalleProducto) {
        return DetalleProductoResponseDTO.builder()
                .id(detalleProducto.getId())
                .cantidad(detalleProducto.getCantidad())
                /*.insumo(insumoService.toDTO(detalleProducto.getInsumo()))*/
                .build();
    }
}
