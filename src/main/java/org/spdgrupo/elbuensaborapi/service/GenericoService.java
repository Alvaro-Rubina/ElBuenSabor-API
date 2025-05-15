package org.spdgrupo.elbuensaborapi.service;

import java.io.Serializable;
import java.util.List;

public interface GenericoService <E , ID extends Serializable>  {
    public E save(E entity);
    public E findById(ID id);
    public List<E> findAll();
    public E update(ID id, E entity);


}
