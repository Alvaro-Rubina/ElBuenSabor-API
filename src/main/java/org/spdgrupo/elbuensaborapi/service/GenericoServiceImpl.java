package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoService;


import java.io.Serializable;
import java.util.List;

public abstract class GenericoServiceImpl<E, D, R, ID extends Serializable> implements GenericoService<E, D, R, ID> {

    protected GenericoRepository<E, ID> genericoRepository;

    public GenericoServiceImpl(GenericoRepository< E , ID> genericoRepository) {
        this.genericoRepository = genericoRepository;
    }

    @Override
    public String save(D entity) {
        
        return genericoRepository.save(entity);
    }

    @Override
    public E findById(ID id) {
        return genericoRepository.findById(id).orElse(null);
    }

    @Override
    public List<E> findAll() {
        return genericoRepository.findAll();
    }

    @Override
    public E update(ID id, E entity) {
        if (genericoRepository.existsById(id)) {
            return genericoRepository.save(entity);
        }
        return null;
    }
}
