package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.CyclicParentException;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.RubroInsumoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    @CacheEvict(value = "rubroInsumos", allEntries = true)
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
    @CachePut(value = "rubroInsumos", key = " 'update_' + #id")
    @Transactional
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

    @Override
    @Transactional
    public void delete(Long id) {
        RubroInsumo rubroInsumo = rubroInsumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + id + " no encontrado"));
        rubroInsumo.setActivo(false);
        rubroInsumoRepository.save(rubroInsumo);
    }

    @Override
    @Cacheable("rubroInsumos")
    public java.util.List<RubroInsumoResponseDTO> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "rubroInsumos", key = "'findById_'+ #id")
    public RubroInsumoResponseDTO findById(Long id) {
        return super.findById(id);
    }
}
