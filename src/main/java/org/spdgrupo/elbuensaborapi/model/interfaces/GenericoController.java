package org.spdgrupo.elbuensaborapi.model.interfaces;

import org.spdgrupo.elbuensaborapi.model.entity.Base;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.io.Serializable;
import java.util.List;


public interface GenericoController <E extends Base, D, R, ID extends Serializable> {
    ResponseEntity<R> save(@RequestBody D entity);
    ResponseEntity<R> findById(@PathVariable ID id);
    ResponseEntity<List<R>> findAll();
}

