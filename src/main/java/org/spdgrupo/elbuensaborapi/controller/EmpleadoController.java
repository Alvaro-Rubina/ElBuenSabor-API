package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.service.EmpleadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @PostMapping("/save")
    public ResponseEntity<String> saveEmpleado(@Valid @RequestBody EmpleadoDTO empleadoDTO) {
        empleadoService.saveEmpleado(empleadoDTO);
        return ResponseEntity.ok("Empleado guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<EmpleadoDTO> getEmpleadoById(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.getEmpleadoById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<EmpleadoDTO>> getAllEmpleados () {
        return ResponseEntity.ok(empleadoService.getAllEmpleados());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmpleado(@PathVariable Long id,
                                                 @Valid @RequestBody EmpleadoDTO empleadoDTO) {
        empleadoService.updateEmpleado(id, empleadoDTO);
        return ResponseEntity.ok("Empleado actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmpleado(@PathVariable Long id) {
        empleadoService.deleteEmpleado(id);
        return ResponseEntity.ok("Empleado eliminado correctamente");
    }
}

