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
    public ResponseEntity<String> save(@Valid @RequestBody D dto) {
        return ResponseEntity.ok("Registro exitoso");
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<R> getById(@PathVariable ID id) {
        R response = servicio.findById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<R>> getAll() {
        return ResponseEntity.ok(servicio.findAll());
    }

}
