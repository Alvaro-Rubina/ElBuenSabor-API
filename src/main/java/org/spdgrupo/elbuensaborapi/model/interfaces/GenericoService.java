package org.spdgrupo.elbuensaborapi.model.interfaces;

import org.spdgrupo.elbuensaborapi.model.entity.Base;

import java.io.Serializable;
import java.util.List;

public interface GenericoService <E extends Base, D, R, ID extends Serializable>  {
    public E save(D dto);
    public R findById(ID id);
    public List<R> findAll();
    public void update(ID id, D entity);
    public void delete(ID id);


}