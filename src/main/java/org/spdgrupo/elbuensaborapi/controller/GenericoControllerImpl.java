

package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
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
    public ResponseEntity<E> save(@RequestBody D dto) {
        return ResponseEntity.ok(servicio.save(dto));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<R> getById(@PathVariable ID id) {
        R response = servicio.findById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<R>> getAll() {
        return ResponseEntity.ok(servicio.findAll());
    }

    //TODO: hacer bien el update
    @Override
    @PutMapping("/generico/{id}")
    public ResponseEntity<String> update(@PathVariable ID id,
                                         @Valid @RequestBody D dto) {
        servicio.update(id, dto);
        return ResponseEntity.ok("Domicilio actualizado correctamente");
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable ID id) {
        servicio.delete(id);
        return ResponseEntity.ok("Entidad eliminada exitosamente");
    }



}
