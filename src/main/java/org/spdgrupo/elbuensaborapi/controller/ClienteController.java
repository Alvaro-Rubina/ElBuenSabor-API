package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.ClienteDTO;
import org.spdgrupo.elbuensaborapi.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ClienteDTO>> getClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.getClienteById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCliente(@RequestBody ClienteDTO clienteDTO) {
        clienteService.saveCliente(clienteDTO);
        return ResponseEntity.ok("Cliente guardado exitosamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCliente(@PathVariable Long id,
                                                @RequestBody ClienteDTO clienteDTO) {
        clienteService.updateCliente(id, clienteDTO);
        return ResponseEntity.ok("Cliente actualizado exitosamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.ok("Cliente eliminado exitosamente");
    }

}
