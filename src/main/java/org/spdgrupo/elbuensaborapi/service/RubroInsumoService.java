package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RubroInsumoService {

    private final RubroInsumoRepository rubroInsumoRepository;

    public void saveRubroInsumo(RubroInsumoDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = toEntity(rubroInsumoDTO);
        rubroInsumoRepository.save(rubroInsumo);
    }

    public RubroInsumoDTO getRubroInsumoById(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));
        return toDTO(rubroInsumo);
    }

    public List<RubroInsumoDTO> getAllRubroInsumos() {
        List<RubroInsumo> rubroInsumos = rubroInsumoRepository.findAll();
        List<RubroInsumoDTO> rubroInsumosDTO = new ArrayList<>();

        for (RubroInsumo rubroInsumo : rubroInsumos) {
            rubroInsumosDTO.add(toDTO(rubroInsumo));
        }
        return rubroInsumosDTO;
    }

    public void updateRubroInsumo(Long id, RubroInsumoDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));

        if(!rubroInsumo.getDenominacion().equals(rubroInsumoDTO.getDenominacion())) {
            rubroInsumo.setDenominacion(rubroInsumoDTO.getDenominacion());
        }

        if (!rubroInsumo.getUnidadMedida().equals(rubroInsumoDTO.getUnidadMedida())) {
            rubroInsumo.setUnidadMedida(rubroInsumoDTO.getUnidadMedida());
        }

        if (rubroInsumo.isActivo() != rubroInsumoDTO.isActivo()) {
            rubroInsumo.setActivo(rubroInsumoDTO.isActivo());
        }

        rubroInsumoRepository.save(rubroInsumo);
    }

    public void deleteRubroInsumo(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));
        rubroInsumo.setActivo(false);
        rubroInsumoRepository.save(rubroInsumo);
    }

    // MAPPERS
    public RubroInsumo toEntity(RubroInsumoDTO rubroInsumoDTO) {
        return RubroInsumo.builder()
                .denominacion(rubroInsumoDTO.getDenominacion())
                .unidadMedida(rubroInsumoDTO.getUnidadMedida())
                .activo(true)
                .rubroPadre(
                        rubroInsumoDTO.getRubroPadre() != null
                                ? rubroInsumoRepository.findById(rubroInsumoDTO.getRubroPadre().getId())
                                .orElseThrow(() -> new NotFoundException("RubroInsumo (padre) con el id " + rubroInsumoDTO.getRubroPadre().getId() + " no encontrado"))
                                : null
                )
                .build();
    }

    public RubroInsumoDTO toDTO(RubroInsumo rubroInsumo) {
        return RubroInsumoDTO.builder()
                .id(rubroInsumo.getId())
                .denominacion(rubroInsumo.getDenominacion())
                .unidadMedida(rubroInsumo.getUnidadMedida())
                .activo(rubroInsumo.isActivo())
                .rubroPadre(rubroInsumo.getRubroPadre() != null ? toDTO(rubroInsumo.getRubroPadre()) : null)
                .build();
    }
}
