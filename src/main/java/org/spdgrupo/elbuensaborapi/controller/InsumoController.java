package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;

import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
public class InsumoController extends GenericoControllerImpl<
        Insumo,
        InsumoDTO,
        InsumoResponseDTO,
        Long,
        InsumoService> {

    @Autowired
    private InsumoService insumoService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public InsumoController(InsumoService insumoService) {
        super(insumoService);
        this.insumoService = insumoService;
    }

    @Override
    @PostMapping("/save")
    public ResponseEntity<InsumoResponseDTO> save(@Valid @RequestBody InsumoDTO insumoDTO) {
        InsumoResponseDTO insumo = insumoService.save(insumoDTO);
        messagingTemplate.convertAndSend("/topic/insumos", insumo);
        return ResponseEntity.ok(insumo);
    }

    @GetMapping("/vendibles")
    public ResponseEntity<List<InsumoResponseDTO>> findAllVendibles() {
        List<InsumoResponseDTO> insumos = insumoService.findAllVendibles();
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<InsumoResponseDTO>> findAllActivos() {
        List<InsumoResponseDTO> insumos = insumoService.findAllActivos();
        return ResponseEntity.ok(insumos);
    }


    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<InsumoResponseDTO>> getInsumosByDenominacion(@RequestParam String denominacion) {
        return ResponseEntity.ok(insumoService.getInsumosByDenominacion(denominacion));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<InsumoResponseDTO> updateInsumo(@PathVariable Long id,
                                               @Valid @RequestBody InsumoDTO insumoDTO) {
        InsumoResponseDTO insumo = insumoService.update(id, insumoDTO);
        messagingTemplate.convertAndSend("/topic/insumos", insumo);
        return ResponseEntity.ok(insumo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInsumo(@PathVariable Long id) {
        insumoService.delete(id);
        messagingTemplate.convertAndSend("/topic/insumos", id);
        return ResponseEntity.ok("Insumo eliminado exitosamente");
    }

    @Override
    @PutMapping("/toggle-activo/{id}")
    public ResponseEntity<String> toggleActivo(@PathVariable Long id) {
        String response = insumoService.toggleActivo(id);
        messagingTemplate.convertAndSend("/topic/insumos", response);
        return ResponseEntity.ok(response);
    }

}
