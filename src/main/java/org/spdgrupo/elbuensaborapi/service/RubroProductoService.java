package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.RubroProductoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RubroProductoService extends GenericoServiceImpl<RubroProducto, RubroProductoDTO, RubroProductoResponseDTO, Long> {

    // Dependencias
    @Autowired
    private RubroProductoRepository rubroProductoRepository;
    @Autowired
    private RubroProductoMapper rubroProductoMapper;

    public RubroProductoService(GenericoRepository<RubroProducto,Long> genericoRepository, GenericoMapper<RubroProducto, RubroProductoDTO, RubroProductoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public RubroProductoResponseDTO update(Long id, RubroProductoDTO rubroProductoDTO) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));

        if (!rubroProducto.getDenominacion().equals(rubroProductoDTO.getDenominacion())) {
            rubroProducto.setDenominacion(rubroProductoDTO.getDenominacion());
        }

        if (!rubroProducto.getActivo().equals(rubroProductoDTO.getActivo())) {
            rubroProducto.setActivo(rubroProductoDTO.getActivo());
        }

        return rubroProductoMapper.toResponseDTO(rubroProductoRepository.save(rubroProducto));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));
        rubroProducto.setActivo(false);
        rubroProductoRepository.save(rubroProducto);
    }
}
