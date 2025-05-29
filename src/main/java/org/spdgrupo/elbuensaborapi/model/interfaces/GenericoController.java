package org.spdgrupo.elbuensaborapi.model.interfaces;

import org.spdgrupo.elbuensaborapi.model.entity.Base;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.io.Serializable;
import java.util.List;


public interface GenericoController <E extends Base, D, R, ID extends Serializable> {
    public ResponseEntity<E> save(@RequestBody D entity);
    public ResponseEntity<R> getById(@PathVariable ID id);
    public ResponseEntity<List<R>> getAll();
    public ResponseEntity<String> update(@PathVariable ID id,@RequestBody D entity);
    public ResponseEntity<String> delete(@PathVariable ID id);
}

