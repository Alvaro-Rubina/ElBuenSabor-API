package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.mappers.DetalleProductoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetalleProductoService extends GenericoServiceImpl<DetalleProducto, DetalleProductoDTO, DetalleProductoResponseDTO, Long> {

    // Dependencias
    @Autowired
    private DetalleProductoMapper detalleProductoMapper;
    @Autowired
    private InsumoRepository insumoRepository;

    public DetalleProductoService(
        GenericoRepository<DetalleProducto, Long> genericoRepository,
        GenericoMapper<DetalleProducto, DetalleProductoDTO, DetalleProductoResponseDTO> genericoMapper
    ) {
        super(genericoRepository, genericoMapper);
    }

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
