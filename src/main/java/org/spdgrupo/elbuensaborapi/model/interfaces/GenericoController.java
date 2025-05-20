package org.spdgrupo.elbuensaborapi.model.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.io.Serializable;


public interface GenericoController <E, ID extends Serializable> {
    public ResponseEntity<?> save(@RequestBody E entity);
    public ResponseEntity<?> getById(@PathVariable ID id);
    public ResponseEntity<?> getAll();
    public ResponseEntity<?> update(@PathVariable ID id,@RequestBody E entity);
}
