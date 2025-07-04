package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
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

    @Override
    @PostMapping("/save")
    public ResponseEntity<R> save(@Valid @RequestBody D dto) {
        R response = servicio.save(dto);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<R> findById(@PathVariable ID id) {
        R response = servicio.findById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<R>> findAll() {
        return ResponseEntity.ok(servicio.findAll());
    }

    @PutMapping("/toggle-activo/{id}")
    public ResponseEntity<String> toggleActivo(@PathVariable ID id) {
        String response = servicio.toggleActivo(id);
        return ResponseEntity.ok(response);
    }

}
