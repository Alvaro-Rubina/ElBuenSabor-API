package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Empleado;
import org.spdgrupo.elbuensaborapi.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empleados")

public class EmpleadoController extends GenericoControllerImpl<
        Empleado,
        EmpleadoDTO,
        EmpleadoResponseDTO,
        Long,
        EmpleadoService> {

    @Autowired
    private EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        super(empleadoService);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmpleado(@PathVariable Long id,
                                                 @Valid @RequestBody EmpleadoDTO empleadoDTO) {
        empleadoService.update(id, empleadoDTO);
        return ResponseEntity.ok("Empleado actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmpleado(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.ok("Empleado eliminado correctamente");
    }
}

