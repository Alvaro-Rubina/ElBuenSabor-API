package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.service.DomicilioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
@RequiredArgsConstructor
public class DomicilioController {

    private final DomicilioService domicilioService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DomicilioDTO>> getDomicilios() {
        return ResponseEntity.ok(domicilioService.getAllDomicilios());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DomicilioDTO> getDomicilioById(@PathVariable Long id) {
        return ResponseEntity.ok(domicilioService.getDomicilioById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDomicilio(@RequestBody DomicilioDTO domicilioDTO) {
        domicilioService.saveDomicilio(domicilioDTO);
        return ResponseEntity.ok("Domicilio guardado exitosamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDomicilio(@PathVariable Long id,
                                                  @RequestBody DomicilioDTO domicilioDTO) {
        domicilioService.updateDomicilio(id, domicilioDTO);
        return ResponseEntity.ok("Domicilio actualizado exitosamente");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteDomicilio(@PathVariable Long id) {
        domicilioService.deleteDomicilio(id);
        return ResponseEntity.ok("Domicilio eliminado exitosamente");
    }

}
