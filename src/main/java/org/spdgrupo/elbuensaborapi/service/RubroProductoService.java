package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RubroProductoService {

    @Autowired
    private RubroProductoRepository rubroProductoRepository;

    public void saveRubroProducto(RubroProductoDTO rubroProductoDTO) {
        RubroProducto rubroProducto = toEntity(rubroProductoDTO);
        rubroProductoRepository.save(rubroProducto);
    }

    public RubroProductoDTO getRubroProducto(Long id) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));
        return toDTO(rubroProducto);
    }

    public List<RubroProductoDTO> getAllRubroProductos() {
        List<RubroProducto> rubroProductos = rubroProductoRepository.findAll();
        List<RubroProductoDTO> rubroProductosDTO = new ArrayList<>();

        for (RubroProducto rubroProducto : rubroProductos) {
            rubroProductosDTO.add(toDTO(rubroProducto));
        }
        return rubroProductosDTO;
    }

    public void editRubroProducto(Long id, RubroProductoDTO rubroProductoDTO) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));

        if(!rubroProducto.getDenominacion().equals(rubroProductoDTO.getDenominacion())) {
            rubroProducto.setDenominacion(rubroProductoDTO.getDenominacion());
        }

        if(!rubroProducto.isActivo() == rubroProductoDTO.isActivo()) {
            rubroProducto.setActivo(rubroProductoDTO.isActivo());
        }

        rubroProductoRepository.save(rubroProducto);
    }

    // MAPPERS
    public RubroProducto toEntity(RubroProductoDTO rubroProductoDTO) {
        return RubroProducto.builder()
                .denominacion(rubroProductoDTO.getDenominacion())
                .activo(rubroProductoDTO.isActivo())
                .build();
    };

    public RubroProductoDTO toDTO(RubroProducto rubroProducto) {
        return RubroProductoDTO.builder()
                .id(rubroProducto.getId())
                .denominacion(rubroProducto.getDenominacion())
                .activo(rubroProducto.isActivo())
                .build();
    }
}
