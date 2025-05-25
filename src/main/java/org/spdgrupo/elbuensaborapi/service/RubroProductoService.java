package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.RubroProductoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RubroProductoService {

    // Dependencias
    private final RubroProductoRepository rubroProductoRepository;
    private final RubroProductoMapper rubroProductoMapper;

    public void saveRubroProducto(RubroProductoDTO rubroProductoDTO) {
        RubroProducto rubroProducto = rubroProductoMapper.toEntity(rubroProductoDTO);
        rubroProductoRepository.save(rubroProducto);
    }

    public RubroProductoResponseDTO getRubroProductoById(Long id) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));
        return rubroProductoMapper.toResponseDTO(rubroProducto);
    }

    public List<RubroProductoResponseDTO> getAllRubroProductos() {
        return rubroProductoRepository.findAll().stream()
                .map(rubroProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void updateRubroProducto(Long id, RubroProductoPatchDTO rubroProductoDTO) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));

        if(rubroProductoDTO.getDenominacion() != null) {
            rubroProducto.setDenominacion(rubroProductoDTO.getDenominacion());
        }

        if(rubroProductoDTO.getActivo() != null) {
            rubroProducto.setActivo(rubroProductoDTO.getActivo());
        }

        rubroProductoRepository.save(rubroProducto);
    }

    public void deleteRubroProducto(Long id) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));
        rubroProducto.setActivo(false);
        rubroProductoRepository.save(rubroProducto);
    }
}
