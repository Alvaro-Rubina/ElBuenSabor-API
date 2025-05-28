package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.CyclicParentException;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.RubroInsumoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RubroInsumoService extends GenericoServiceImpl<RubroInsumo, RubroInsumoDTO, RubroInsumoResponseDTO, Long> {

    // Dependencias
    @Autowired
    private RubroInsumoRepository rubroInsumoRepository;
    @Autowired
    private RubroInsumoMapper rubroInsumoMapper;

    public RubroInsumoService(GenericoRepository<RubroInsumo,Long> rubroInsumoRepository, GenericoMapper<RubroInsumo,RubroInsumoDTO,RubroInsumoResponseDTO> rubroInsumoMapper) {
        super(rubroInsumoRepository, rubroInsumoMapper);
    }

    @Override
    public RubroInsumo save(RubroInsumoDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = rubroInsumoMapper.toEntity(rubroInsumoDTO);

        if (rubroInsumoDTO.getRubroPadreId() != null) {
            RubroInsumo rubroPadre = rubroInsumoRepository.findById(rubroInsumoDTO.getRubroPadreId())
                    .orElseThrow(() -> new NotFoundException("RubroInsumo padre con el id " + rubroInsumoDTO.getRubroPadreId() + " no encontrado"));
            rubroInsumo.setRubroPadre(rubroPadre);
        }

        return (rubroInsumoRepository.save(rubroInsumo));
    }

    @Override
    public void update(Long id, RubroInsumoDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));

        // Actualizamos todos los campos
        rubroInsumo.setDenominacion(rubroInsumoDTO.getDenominacion());
        rubroInsumo.setActivo(rubroInsumoDTO.getActivo());

        // Validación y actualización del rubro padre
        if (rubroInsumoDTO.getRubroPadreId() != null) {
            // Validación contra ciclos
            if (rubroInsumoDTO.getRubroPadreId().equals(id)) {
                throw new CyclicParentException("No se puede asignar el rubro a sí mismo como padre");
            }

            boolean isChild = rubroInsumo.getSubRubros().stream()
                    .anyMatch(subRubro -> subRubro.getId().equals(rubroInsumoDTO.getRubroPadreId()));
            if (isChild) {
                throw new CyclicParentException("No se puede asignar como padre de un rubro a uno de sus hijos");
            }

            RubroInsumo rubroPadre = rubroInsumoRepository.findById(rubroInsumoDTO.getRubroPadreId())
                    .orElseThrow(() -> new NotFoundException("Rubro padre no encontrado"));
            rubroInsumo.setRubroPadre(rubroPadre);
        } else {
            rubroInsumo.setRubroPadre(null);
        }

        rubroInsumoRepository.save(rubroInsumo);
    }


    public void delete(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));
        rubroInsumo.setActivo(false);
        rubroInsumoRepository.save(rubroInsumo);
    }
}
