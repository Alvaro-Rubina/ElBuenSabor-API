package org.spdgrupo.elbuensaborapi.model.interfaces;

import org.spdgrupo.elbuensaborapi.model.entity.Base;

import java.io.Serializable;
import java.util.List;

public interface GenericoService <E extends Base, D, R, ID extends Serializable>  {
    void save(D dto);
    R findById(ID id);
    List<R> findAll();
    void update(ID id, D entity);
    void delete(ID id);
    void toggleActivo(ID id);


}