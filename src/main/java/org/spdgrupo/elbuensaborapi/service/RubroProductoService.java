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
        List<RubroProductoDTO> rubroProductoDTO = new ArrayList<RubroProductoDTO>();

        for (RubroProducto rubroProducto : rubroProductos) {
            rubroProductoDTO.add(toDTO(rubroProducto));
        }
        return rubroProductoDTO;
    }

    public void editRubroProducto(Long id, RubroProductoDTO rubroProductoDTO) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));

        if(!rubroProducto.getId().equals(rubroProductoDTO.getId())) {
            rubroProducto.setId(rubroProductoDTO.getId());
        }

        if(!rubroProducto.getUnidadMedida().equals(rubroProductoDTO.getUnidadMedida())) {
            rubroProducto.setUnidadMedida(rubroProductoDTO.getUnidadMedida());
        }

        if(!rubroProducto.isActivo() == rubroProductoDTO.isActivo()) {
            rubroProducto.setActivo(rubroProductoDTO.isActivo());
        }

        rubroProductoRepository.save(rubroProducto);
    }

    // MAPPERS
    private RubroProducto toEntity(RubroProductoDTO rubroProductoDTO) {
        return RubroProducto.builder()
                .denominacion(rubroProductoDTO.getDenominacion())
                .unidadMedida(rubroProductoDTO.getUnidadMedida())
                .activo(rubroProductoDTO.isActivo())
                .build();
    };

    private RubroProductoDTO toDTO(RubroProducto rubroProducto) {
        return RubroProductoDTO.builder()
                .id(rubroProducto.getId())
                .denominacion(rubroProducto.getDenominacion())
                .unidadMedida(rubroProducto.getUnidadMedida())
                .activo(rubroProducto.isActivo())
                .build();
    }
}
