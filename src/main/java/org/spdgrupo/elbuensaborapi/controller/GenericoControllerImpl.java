

package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.entity.Base;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoController;
import org.spdgrupo.elbuensaborapi.service.GenericoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;



public abstract class GenericoControllerImpl<E extends Base, D, R, ID extends Serializable,
        S extends GenericoServiceImpl<E, D, R, ID>> implements GenericoController<E, D, R, ID> {

    protected S servicio;

    protected GenericoControllerImpl(S servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/save")
    public ResponseEntity<E> save(@RequestBody D dto) {
        return ResponseEntity.ok(servicio.save(dto));
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

    //TODO: hacer bien el update
    /*@PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable ID id, @RequestBody D dto) {

        return ResponseEntity.ok();
    }*/
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable ID id,@RequestBody D dto) {
        // Implementación aquí
        return null;
    }

}
