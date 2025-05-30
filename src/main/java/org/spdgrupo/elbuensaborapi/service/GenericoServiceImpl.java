package org.spdgrupo.elbuensaborapi.service;

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
    public E save(D dto){
        E entity = genericoMapper.toEntity(dto);
        genericoRepository.save(entity);
        return entity;
    }

    @Override
    public R findById(ID id){
        E entity = genericoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entidad con el id " + id + " no encontrado"));
        return genericoMapper.toResponseDTO(entity);
    }

    @Override
    public List<R> findAll(){
        return genericoRepository.findAll().stream()
                .map(genericoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    //TODO: hacer bien el metodo update
    @Override
    @Transactional
    public void update(ID id, D dto) {
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (!genericoRepository.existsById(id)) {
            throw new RuntimeException("Entidad con el id " + id + " no encontrada");
        }
        genericoRepository.deleteById(id);
    }

}
