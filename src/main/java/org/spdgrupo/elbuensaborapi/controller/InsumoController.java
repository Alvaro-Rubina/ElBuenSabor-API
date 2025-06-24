package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;

import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    public InsumoController(InsumoService insumoService) {
        super(insumoService);
        this.insumoService = insumoService;
    }

    @GetMapping("vendibles")
    public ResponseEntity<List<InsumoResponseDTO>> findAllVendibles() {
        List<InsumoResponseDTO> insumos = insumoService.findAllVendibles();
        return ResponseEntity.ok(insumos);
    }


    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<InsumoResponseDTO>> getInsumosByDenominacion(@RequestParam String denominacion) {
        return ResponseEntity.ok(insumoService.getInsumosByDenominacion(denominacion));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateInsumo(@PathVariable Long id,
                                               @Valid @RequestBody InsumoDTO insumoDTO) {
        insumoService.update(id, insumoDTO);
        return ResponseEntity.ok("Insumo actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInsumo(@PathVariable Long id) {
        insumoService.delete(id);
        return ResponseEntity.ok("Insumo eliminado exitosamente");
    }

}
