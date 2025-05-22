package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RubroProductoService {

    // Dependencias
    private final RubroProductoRepository rubroProductoRepository;

    public void saveRubroProducto(RubroProductoDTO rubroProductoDTO) {
        RubroProducto rubroProducto = toEntity(rubroProductoDTO);
        rubroProductoRepository.save(rubroProducto);
    }

    public RubroProductoResponseDTO getRubroProductoById(Long id) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));
        return toDTO(rubroProducto);
    }

    public List<RubroProductoResponseDTO> getAllRubroProductos() {
        List<RubroProducto> rubroProductos = rubroProductoRepository.findAll();
        List<RubroProductoResponseDTO> rubroProductosDTO = new ArrayList<>();

        for (RubroProducto rubroProducto : rubroProductos) {
            rubroProductosDTO.add(toDTO(rubroProducto));
        }
        return rubroProductosDTO;
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

    // MAPPERS
    public RubroProducto toEntity(RubroProductoDTO rubroProductoDTO) {
        return RubroProducto.builder()
                .denominacion(rubroProductoDTO.getDenominacion())
                .activo(true) // cuando se guarda un rubro siempre es activo = true
                .build();
    };

    public RubroProductoResponseDTO toDTO(RubroProducto rubroProducto) {
        return RubroProductoResponseDTO.builder()
                .id(rubroProducto.getId())
                .denominacion(rubroProducto.getDenominacion())
                .activo(rubroProducto.getActivo())
                .build();
    }
}
