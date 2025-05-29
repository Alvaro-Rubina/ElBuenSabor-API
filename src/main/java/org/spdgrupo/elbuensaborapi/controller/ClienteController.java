package org.spdgrupo.elbuensaborapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/save")
    public ResponseEntity<String> saveCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        clienteService.saveCliente(clienteDTO);
        return ResponseEntity.ok("Cliente guardado correctamente");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.getClienteById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ClienteResponseDTO>> getClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCliente(@PathVariable Long id,
                                                @Valid @RequestBody ClienteDTO clienteDTO) {
        clienteService.updateCliente(id, clienteDTO);
        return ResponseEntity.ok("Cliente actualizado correctamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.ok("Cliente eliminado exitosamente");
    }

}
