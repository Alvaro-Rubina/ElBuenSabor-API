package org.spdgrupo.elbuensaborapi.controller;

import com.auth0.exception.Auth0Exception;
import jakarta.validation.Valid;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/save")
    public ResponseEntity<ClienteResponseDTO> save(@Valid @RequestBody ClienteDTO clienteDTO) throws Auth0Exception {
        ClienteResponseDTO cliente = clienteService.save(clienteDTO);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponseDTO> findByEmail(@PathVariable String email) {
        ClienteResponseDTO cliente = clienteService.findByEmail(email);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable Long id,
                                                @Valid @RequestBody ClienteDTO clienteDTO) throws Auth0Exception {
        ClienteResponseDTO cliente = clienteService.update(id, clienteDTO);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.ok("Cliente eliminado exitosamente");
    }

    @DeleteMapping("delete/physical/{id}")
    public ResponseEntity<String> deletePhysically(@PathVariable Long id) throws Auth0Exception {
        clienteService.deletePhysically(id);
        return ResponseEntity.ok("Cliente eliminado fisicamente");
    }

    @PutMapping("/toggle-activo/{id}")
    public ResponseEntity<String> toggleActivo(@PathVariable Long id) {
        clienteService.toggleActivo(id);
        return ResponseEntity.ok("Cliente actualizado exitosamente");
    }

}
