package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RubroInsumoService {

    // Dependencias
    private final RubroInsumoRepository rubroInsumoRepository;

    public void saveRubroInsumo(RubroInsumoDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = toEntity(rubroInsumoDTO);
        rubroInsumoRepository.save(rubroInsumo);
    }

    public RubroInsumoResponseDTO getRubroInsumoById(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));
        return toDTO(rubroInsumo);
    }

    public List<RubroInsumoResponseDTO> getAllRubroInsumos() {
        List<RubroInsumo> rubroInsumos = rubroInsumoRepository.findAll();
        List<RubroInsumoResponseDTO> rubroInsumosDTO = new ArrayList<>();

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
        if (!rubroInsumo.isActivo() == rubroInsumoDTO.isActivo()) {
            rubroInsumo.setActivo(rubroInsumoDTO.isActivo());
        }
        // El RubroInsumo no tiene padre pero el DTO SI
        if (rubroInsumo.getRubroPadre() == null && rubroInsumoDTO.getRubroPadreId() != null) {
            rubroInsumo.setRubroPadre(rubroInsumoRepository.findById(rubroInsumoDTO.getRubroPadreId())
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + rubroInsumoDTO.getRubroPadreId() + " no encontrado")));

        // El RubroInsumo SI tiene padre pero es DISTINTO al del DTO
        } else if (rubroInsumo.getRubroPadre() != null && (!rubroInsumo.getRubroPadre().getId().equals(rubroInsumoDTO.getRubroPadreId()))) {
            rubroInsumo.setRubroPadre(rubroInsumoRepository.findById(rubroInsumoDTO.getRubroPadreId())
                    .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + rubroInsumoDTO.getRubroPadreId() + " no encontrado")));

        // El RubroInsumo SI tiene padre pero el DTO NO
        } else if (rubroInsumo.getRubroPadre() != null && rubroInsumoDTO.getRubroPadreId() == null) {
            rubroInsumo.setRubroPadre(null);
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
                .activo(true) // cuando se guarda un rubro siempre es activo = true
                .rubroPadre(rubroInsumoDTO.getRubroPadreId() == null ? null :
                        rubroInsumoRepository.findById(rubroInsumoDTO.getRubroPadreId())
                                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + rubroInsumoDTO.getRubroPadreId() + " no encontrado")))
                .build();
    }

    public RubroInsumoResponseDTO toDTO(RubroInsumo rubroInsumo) {
        return RubroInsumoResponseDTO.builder()
                .id(rubroInsumo.getId())
                .denominacion(rubroInsumo.getDenominacion())
                .activo(rubroInsumo.isActivo())
                .rubroPadre(rubroInsumo.getRubroPadre() == null ? null : toDTO(rubroInsumo.getRubroPadre()))
                .build();
    }
}
