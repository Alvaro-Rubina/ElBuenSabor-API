package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.spdgrupo.elbuensaborapi.model.entity.Base;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoService;


import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericoServiceImpl<E extends Base, D, R, ID extends Serializable> implements GenericoService<E, D, R, ID> {

    protected GenericoRepository<E, ID> genericoRepository;
    protected GenericoMapper<E, D, R> genericoMapper;

    public GenericoServiceImpl(GenericoRepository<E, ID> genericoRepository, GenericoMapper<E, D, R> genericoMapper) {
        this.genericoRepository = genericoRepository;
        this.genericoMapper = genericoMapper;
    }

    @Override
    @Transactional
    public R save(D dto){
        E entity = genericoMapper.toEntity(dto);
        genericoRepository.save(entity);
        return genericoMapper.toResponseDTO(entity);
    }

    @Override
    public R findById(ID id){
        E entity = genericoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entidad con el id " + id + " no encontrado"));
        return genericoMapper.toResponseDTO(entity);
    }

    @Override
    public List<R> findAll(){
        return genericoRepository.findAll().stream()
                .map(genericoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Este metodo no tiene implementación definida porque se sobreescribe y tiene implementación propia en cada service
    @Override
    @Transactional
    public R update(ID id, D dto) {
        return null;
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (!genericoRepository.existsById(id)) {
            throw new NotFoundException("Entidad con el id " + id + " no encontrada");
        }
        genericoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleActivo(ID id) {
        E entity = genericoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entidad con el id " + id + " no encontrada"));
        entity.setActivo(!entity.getActivo());
        genericoRepository.save(entity);
    }

}
