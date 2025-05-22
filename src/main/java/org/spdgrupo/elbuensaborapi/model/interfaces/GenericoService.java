package org.spdgrupo.elbuensaborapi.model.interfaces;

import java.io.Serializable;
import java.util.List;

public interface GenericoService <E, D , R, ID extends Serializable>  {
    public R save(D entity);
    public R findById(ID id);
    public List<R> findAll();
    public String update(ID id, D entity);


}