package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.ClienteDTO;
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

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ClienteDTO>> getClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ClienteDTO> getClienteById(Long id) {
        return ResponseEntity.ok(clienteService.getClienteById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCliente(@RequestBody ClienteDTO clienteDTO) {
        clienteService.saveCliente(clienteDTO);
        return ResponseEntity.ok("Cliente guardado exitosamente");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateCliente(ClienteDTO clienteDTO, @PathVariable Long id) {
        clienteService.updateCliente(clienteDTO, id);
        return ResponseEntity.ok("Cliente actualizado exitosamente");
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteCliente(Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.ok("Cliente eliminado exitosamente");
    }

}
