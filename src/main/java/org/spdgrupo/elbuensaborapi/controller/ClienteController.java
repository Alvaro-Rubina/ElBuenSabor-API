package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.ClienteDTO;
import org.spdgrupo.elbuensaborapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(Long id) {
        return ResponseEntity.ok(clienteService.getClienteById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCliente(ClienteDTO clienteDTO) {
        clienteService.saveCliente(clienteDTO);
        return ResponseEntity.ok("Cliente guardado exitosamente");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCliente(ClienteDTO clienteDTO, Long id) {
        clienteService.updateCliente(clienteDTO, id);
        return ResponseEntity.ok("Cliente actualizado exitosamente");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteCliente(Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.ok("Cliente eliminado exitosamente");
    }

}
