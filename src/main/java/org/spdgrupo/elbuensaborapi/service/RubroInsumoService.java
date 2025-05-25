package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.CyclicParentException;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.RubroInsumoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RubroInsumoService {

    // Dependencias
    private final RubroInsumoRepository rubroInsumoRepository;
    private final RubroInsumoMapper rubroInsumoMapper;

    public void saveRubroInsumo(RubroInsumoDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = rubroInsumoMapper.toEntity(rubroInsumoDTO);

        if (rubroInsumoDTO.getRubroPadreId() != null) {
            RubroInsumo rubroPadre = rubroInsumoRepository.findById(rubroInsumoDTO.getRubroPadreId())
                    .orElseThrow(() -> new NotFoundException("RubroInsumo padre con el id " + rubroInsumoDTO.getRubroPadreId() + " no encontrado"));
            rubroInsumo.setRubroPadre(rubroPadre);
        }

        rubroInsumoRepository.save(rubroInsumo);
    }

    public RubroInsumoResponseDTO getRubroInsumoById(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));
        return rubroInsumoMapper.toResponseDTO(rubroInsumo);
    }

    public List<RubroInsumoResponseDTO> getAllRubroInsumos() {
        return rubroInsumoRepository.findAll().stream()
                .map(rubroInsumoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void updateRubroInsumo(Long id, RubroInsumoPatchDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));

        List<RubroInsumo> subRubros = rubroInsumo.getSubRubros();

        if (rubroInsumoDTO.getDenominacion() != null) {
            rubroInsumo.setDenominacion(rubroInsumoDTO.getDenominacion());
        }

        if (rubroInsumoDTO.getActivo() != null) {
            rubroInsumo.setActivo(rubroInsumoDTO.getActivo());
        }

        if (rubroInsumoDTO.getRubroPadreId().isPresent()) {
            Long padreId = rubroInsumoDTO.getRubroPadreId().get();

            // validaciones contra ciclos
            if (padreId.equals(id)) {
                throw new CyclicParentException("No se puede asignar el rubro a sí mismo como padre");
            }

            boolean isChild = subRubros.stream().anyMatch(subRubro -> subRubro.getId().equals(padreId));
            if (isChild) {
                throw new CyclicParentException("No se puede asignar como padre de un rubro a uno de sus hijos");
            }

            RubroInsumo rubroPadre = rubroInsumoRepository.findById(padreId)
                    .orElseThrow(() -> new NotFoundException("Rubro padre no encontrado"));
            rubroInsumo.setRubroPadre(rubroPadre);
        }

        rubroInsumoRepository.save(rubroInsumo);
    }


    public void deleteRubroInsumo(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));
        rubroInsumo.setActivo(false);
        rubroInsumoRepository.save(rubroInsumo);
    }
}
