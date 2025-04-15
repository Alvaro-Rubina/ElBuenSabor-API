package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.service.DomicilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
public class DomicilioController {

    @Autowired
    private DomicilioService domicilioService;

    @GetMapping
    public ResponseEntity<List<DomicilioDTO>> getDomicilios() {
        return ResponseEntity.ok(domicilioService.getAllDomicilios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DomicilioDTO> getDomicilioById(Long id) {
        return ResponseEntity.ok(domicilioService.getDomicilioById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDomicilio(DomicilioDTO domicilioDTO) {
        domicilioService.saveDomicilio(domicilioDTO);
        return ResponseEntity.ok("Domicilio guardado exitosamente");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateDomicilio(DomicilioDTO domicilioDTO) {
        domicilioService.updateDomicilio(domicilioDTO);
        return ResponseEntity.ok("Domicilio actualizado exitosamente");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteDomicilio(Long id) {
        domicilioService.deleteDomicilio(id);
        return ResponseEntity.ok("Domicilio eliminado exitosamente");
    }




}
