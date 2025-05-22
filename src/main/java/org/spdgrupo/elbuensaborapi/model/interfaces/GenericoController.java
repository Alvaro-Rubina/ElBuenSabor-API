package org.spdgrupo.elbuensaborapi.model.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.io.Serializable;
import java.util.List;


public interface GenericoController <E, D, R, ID extends Serializable> {
    public ResponseEntity<String> save(@RequestBody D entity);
    public ResponseEntity<R> getById(@PathVariable ID id);
    public ResponseEntity<List<R>> getAll();
    public ResponseEntity<String> update(@PathVariable ID id,@RequestBody D entity);
}

