package org.spdgrupo.elbuensaborapi.controller;

import com.auth0.exception.Auth0Exception;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.rol.RolDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rol.RolResponseDTO;
import org.spdgrupo.elbuensaborapi.service.RolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@Valid @RequestBody RolDTO rolDTO) throws Auth0Exception {
        rolService.save(rolDTO);
        return ResponseEntity.ok("Registro exitoso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDTO> findById(@PathVariable Long id) {
        RolResponseDTO rol = rolService.findById(id);
        return ResponseEntity.ok(rol);
    }

    @GetMapping("/auth0/{auth0Id}")
    public ResponseEntity<RolResponseDTO> findByAuth0Id(@PathVariable String auth0Id) {
        RolResponseDTO rol = rolService.findByAuth0RolId(auth0Id);
        return ResponseEntity.ok(rol);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RolResponseDTO> findByNombre(@PathVariable String nombre) {
        RolResponseDTO rol = rolService.findByNombre(nombre);
        return ResponseEntity.ok(rol);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<RolResponseDTO>> findAll() {
        List<RolResponseDTO> roles = rolService.findAll();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/update/auth0/{auth0Id}")
    public ResponseEntity<RolResponseDTO> update(@PathVariable String auth0Id,
                                         @Valid @RequestBody RolDTO rolDTO) throws Auth0Exception {
        RolResponseDTO rol = rolService.update(auth0Id, rolDTO);
        return ResponseEntity.ok(rol);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        rolService.delete(id);
        return ResponseEntity.ok("Rol eliminado correctamente");
    }

    @DeleteMapping("/delete/auth0/{auth0Id}")
    public ResponseEntity<String> deleteByAuth0Id(@PathVariable String auth0Id) {
        rolService.deleteByAuth0RolId(auth0Id);
        return ResponseEntity.ok("Rol eliminado correctamente");
    }

    @DeleteMapping("/delete/physical/auth0/{auth0Id}")
    public ResponseEntity<String> physicalDeleteByAuth0Id(@PathVariable String auth0Id) throws Auth0Exception {
        rolService.deletePhysicallyByAuth0RolId(auth0Id);
        return ResponseEntity.ok("Rol eliminado físicamente");
    }

    @PutMapping("/toggle-activo/{id}")
    public ResponseEntity<String> toggleActivo(@PathVariable Long id) {
        rolService.toggleActivo(id);
        return ResponseEntity.ok("Rol actualizado exitosamente");
    }

}
