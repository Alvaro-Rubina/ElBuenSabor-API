package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.service.DomicilioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
@RequiredArgsConstructor
public class DomicilioController {

    private final DomicilioService domicilioService;

    @PostMapping("/save")
    public ResponseEntity<String> saveDomicilio(@Valid @RequestBody DomicilioDTO domicilioDTO) {
        domicilioService.saveDomicilio(domicilioDTO);
        return ResponseEntity.ok("Domicilio guardado exitosamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DomicilioResponseDTO> getDomicilioById(@PathVariable Long id) {
        return ResponseEntity.ok(domicilioService.getDomicilioById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DomicilioResponseDTO>> getDomicilios() {
        return ResponseEntity.ok(domicilioService.getAllDomicilios());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateDomicilio(@PathVariable Long id,
                                                  @RequestBody DomicilioPatchDTO domicilioDTO) {
        domicilioService.updateDomicilio(id, domicilioDTO);
        return ResponseEntity.ok("Domicilio actualizado correctamente");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteDomicilio(@PathVariable Long id) {
        domicilioService.deleteDomicilio(id);
        return ResponseEntity.ok("Domicilio eliminado correctamente");
    }

}
