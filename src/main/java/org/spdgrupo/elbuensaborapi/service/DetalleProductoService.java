package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.mappers.DetalleProductoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetalleProductoService {

    // Dependencias
    private final DetalleProductoMapper detalleProductoMapper;
    private final InsumoRepository insumoRepository;

    @Transactional
    public DetalleProducto createDetalleProducto(DetalleProductoDTO detalleProductoDTO) {
        DetalleProducto detalleProducto = detalleProductoMapper.toEntity(detalleProductoDTO);
        detalleProducto.setInsumo(insumoRepository.findById(detalleProductoDTO.getInsumoId())
                .orElseThrow(() -> new IllegalArgumentException("Insumo con el id " + detalleProductoDTO.getInsumoId() + " no encontrado")));
        return detalleProducto;
    }

    public DetalleProductoResponseDTO getDetalleProducto(DetalleProducto detalleProducto) {
        return detalleProductoMapper.toResponseDTO(detalleProducto);
    }
}
