package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.CyclicParentException;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.RubroInsumoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RubroInsumoService extends GenericoServiceImpl<RubroInsumo, RubroInsumoDTO, RubroInsumoResponseDTO, Long> {

    // Dependencias
    @Autowired
    private RubroInsumoRepository rubroInsumoRepository;
    @Autowired
    private RubroInsumoMapper rubroInsumoMapper;
    @Autowired
    private InsumoService insumoService;

    public RubroInsumoService(GenericoRepository<RubroInsumo,Long> rubroInsumoRepository, GenericoMapper<RubroInsumo,RubroInsumoDTO,RubroInsumoResponseDTO> rubroInsumoMapper) {
        super(rubroInsumoRepository, rubroInsumoMapper);
    }


    @Override
    @Transactional
    public RubroInsumoResponseDTO save(RubroInsumoDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = rubroInsumoMapper.toEntity(rubroInsumoDTO);

        if (rubroInsumoDTO.getRubroPadreId() != null) {
            RubroInsumo rubroPadre = rubroInsumoRepository.findById(rubroInsumoDTO.getRubroPadreId())
                    .orElseThrow(() -> new NotFoundException("RubroInsumo padre con el id " + rubroInsumoDTO.getRubroPadreId() + " no encontrado"));
            rubroInsumo.setRubroPadre(rubroPadre);
        }

        rubroInsumoRepository.save(rubroInsumo);
        return rubroInsumoMapper.toResponseDTO(rubroInsumo);
    }

    @Override
    @Transactional
    public RubroInsumoResponseDTO update(Long id, RubroInsumoDTO rubroInsumoDTO) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));

        if (!rubroInsumo.getDenominacion().equals(rubroInsumoDTO.getDenominacion())) {
            rubroInsumo.setDenominacion(rubroInsumoDTO.getDenominacion());
        }

        if (!rubroInsumo.getActivo().equals(rubroInsumoDTO.getActivo())) {
            rubroInsumo.setActivo(rubroInsumoDTO.getActivo());
        }

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

        return rubroInsumoMapper.toResponseDTO(rubroInsumoRepository.save(rubroInsumo));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));
        rubroInsumo.setActivo(false);
        rubroInsumoRepository.save(rubroInsumo);
    }

    @Override
    @Transactional
    public String toggleActivo(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));

        Boolean valorAnterior = rubroInsumo.getActivo();
        rubroInsumo.setActivo(!rubroInsumo.getActivo());
        Boolean valorActualizado = rubroInsumo.getActivo();

        if (valorAnterior.equals(false) &&
                (rubroInsumo.getRubroPadre() != null && rubroInsumo.getRubroPadre().getActivo().equals(false))) {
            rubroInsumo.setActivo(valorAnterior);
            throw new CyclicParentException("No se puede activar un rubro cuyo padre está inactivo");
        }

        List<InsumoResponseDTO> insumos = insumoService.findInsumosByRubroId(rubroInsumo.getId());
        if (valorActualizado.equals(false)) {
            for (InsumoResponseDTO insumo: insumos) {
                insumoService.delete(insumo.getId());
            }
        } else {
            for (InsumoResponseDTO insumo: insumos) {
                if (insumo.getStockActual() >= insumo.getStockMinimo()) {
                    insumoService.activate(insumo.getId());
                }
            }
        }

        genericoRepository.save(rubroInsumo);
        return "Estado 'activo' actualizado" +
                "\n- Valor anterior: " + valorAnterior +
                "\n- Valor actualizado: " + valorActualizado;
    }
}
