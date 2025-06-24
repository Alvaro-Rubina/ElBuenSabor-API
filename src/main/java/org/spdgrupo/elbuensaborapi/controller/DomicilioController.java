package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.service.DomicilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")

public class DomicilioController extends GenericoControllerImpl<
        Domicilio,
        DomicilioDTO,
        DomicilioResponseDTO,
        Long,
        DomicilioService> {

    @Autowired
    private DomicilioService domicilioService;

    public DomicilioController(DomicilioService domicilioService) {
        super(domicilioService);
        this.domicilioService = domicilioService;
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<DomicilioResponseDTO>> getDomicilioByClienteId(@PathVariable Long clienteId) {
        List<DomicilioResponseDTO> domicilios = domicilioService.getDomiciliosByClienteId(clienteId);
        return ResponseEntity.ok(domicilios);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                                  @Valid @RequestBody DomicilioDTO domicilioDTO) {
        domicilioService.update(id, domicilioDTO);
        return ResponseEntity.ok("Domicilio actualizado correctamente");
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDomicilio(@PathVariable Long id) {
        domicilioService.delete(id);
        return ResponseEntity.ok("Domicilio eliminado correctamente");
    }

}