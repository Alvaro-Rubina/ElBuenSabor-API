package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoResponseDTO;
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
    public ResponseEntity<EmpleadoResponseDTO> getEmpleadoById(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.getEmpleadoById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<EmpleadoResponseDTO>> getAllEmpleados () {
        return ResponseEntity.ok(empleadoService.getAllEmpleados());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateEmpleado(@PathVariable Long id,
                                                 @RequestBody EmpleadoPatchDTO empleadoDTO) {
        empleadoService.updateEmpleado(id, empleadoDTO);
        return ResponseEntity.ok("Empleado actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmpleado(@PathVariable Long id) {
        empleadoService.deleteEmpleado(id);
        return ResponseEntity.ok("Empleado eliminado correctamente");
    }
}

