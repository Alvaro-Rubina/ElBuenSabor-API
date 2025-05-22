package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoController;
import org.spdgrupo.elbuensaborapi.service.GenericoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public abstract class GenericoControllerImpl<E,S extends GenericoServiceImpl<E,Long>> implements GenericoController<E, Long> {

    protected S servicio;

    @PostMapping("/save")
    public ResponseEntity<E> save(@RequestBody E entity) {
        return ResponseEntity.ok(servicio.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<E> findById(@PathVariable Long id) {
        E entity = servicio.findById(id);
        if (entity != null) {
            return ResponseEntity.ok(entity);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<E>> findAll() {
        return ResponseEntity.ok(servicio.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<E> update(@PathVariable Long id, @RequestBody E entity) {
        E updated = servicio.update(id, entity);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }


}



