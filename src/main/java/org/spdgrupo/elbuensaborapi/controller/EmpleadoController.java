package org.spdgrupo.elbuensaborapi.controller;

import com.auth0.exception.Auth0Exception;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoDTO;
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
    public ResponseEntity<EmpleadoResponseDTO> save(@Valid @RequestBody EmpleadoDTO empleadoDTO) throws Auth0Exception {
        EmpleadoResponseDTO empleado = empleadoService.save(empleadoDTO);
        return ResponseEntity.ok(empleado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> findById(@PathVariable Long id) {
        EmpleadoResponseDTO empleado = empleadoService.findById(id);
        return ResponseEntity.ok(empleado);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmpleadoResponseDTO> findByEmail(@PathVariable String email) {
        EmpleadoResponseDTO empleado = empleadoService.findByEmail(email);
        return ResponseEntity.ok(empleado);
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> findAll() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmpleadoResponseDTO> update(@PathVariable Long id,
                                                 @Valid @RequestBody EmpleadoDTO empleadoDTO) throws Auth0Exception {
        EmpleadoResponseDTO empleado = empleadoService.update(id, empleadoDTO);
        return ResponseEntity.ok(empleado);
    }
    
    @PutMapping("/update/auth0Id/{auth0Id}")
    public ResponseEntity<EmpleadoResponseDTO> updateByAuth0Id(@PathVariable String auth0Id,
                                                               @Valid @RequestBody EmpleadoDTO empleadoDTO) throws Auth0Exception {
        EmpleadoResponseDTO empleado = empleadoService.updateByAuth0Id(auth0Id, empleadoDTO);
        return ResponseEntity.ok(empleado);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.ok("Empleado eliminado correctamente");
    }

    @DeleteMapping("delete/physical/{id}")
    public ResponseEntity<String> deletePhysically(@PathVariable Long id) throws Auth0Exception {
        empleadoService.deletePhysically(id);
        return ResponseEntity.ok("Empleado eliminado fisicamente");
    }

    @PutMapping("/toggle-activo/{id}")
    public ResponseEntity<String> toggleActivo(@PathVariable Long id) {
        empleadoService.toggleActivo(id);
        return ResponseEntity.ok("Empleado actualizado exitosamente");
    }
}

