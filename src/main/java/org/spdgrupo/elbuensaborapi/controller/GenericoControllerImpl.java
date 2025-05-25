/*

package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoController;
import org.spdgrupo.elbuensaborapi.service.GenericoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public abstract class GenericoControllerImpl<E,
        D, R, ID extends Serializable,
        S extends GenericoServiceImpl<E , D, R, ID>
        > implements GenericoController<E, D, R, ID> {

    protected S servicio;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody D dto) {
        servicio.save(dto);
        return ResponseEntity.ok("Guardado correctamente");
    }

    @GetMapping("/{id}")
    public ResponseEntity<R> getById(@PathVariable ID id) {
        R response = servicio.findById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<R>> getAll() {
        return ResponseEntity.ok(servicio.findAll());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable ID id, @RequestBody D dto) {
        boolean updated = servicio.update(id, dto);
        if (updated) {
            return ResponseEntity.ok("Actualizado correctamente");
        }
        return ResponseEntity.notFound().build();
    }
}*/
