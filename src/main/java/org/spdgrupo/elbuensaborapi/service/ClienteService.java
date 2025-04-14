package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    public void saveCliente(ClienteDTO clienteDTO) {
        Cliente cliente = toEntity(clienteDTO);
        clienteRepository.save(cliente);
    }

    public void updateCliente(ClienteDTO clienteDTO, Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!clienteDTO.getNombre().equals(cliente.getNombre())) {
            cliente.setNombre(clienteDTO.getNombre());
        }
        if (!clienteDTO.getApellido().equals(cliente.getApellido())) {
            cliente.setApellido(clienteDTO.getApellido());
        }
        if (!clienteDTO.getTelefono().equals(cliente.getTelefono())) {
            cliente.setTelefono(clienteDTO.getTelefono());
        }
        if (!clienteDTO.getActivo().equals(cliente.getActivo())) {
            cliente.setActivo(clienteDTO.getActivo());
        }
        if (!clienteDTO.getUsuario().getId().equals(cliente.getUsuario().getId())) {
            cliente.setUsuario(usuarioRepository.findById(clienteDTO.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        }

        clienteRepository.save(cliente);
    }

    public ClienteDTO getClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return toDto(cliente);
    }

    public List<ClienteDTO> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDTO> clientesDTO = new ArrayList<>();
        for (Cliente cliente : clientes) {
            clientesDTO.add(toDto(cliente));
        }
        return clientesDTO;
    }

    public void deleteCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }


    private Cliente toEntity(ClienteDTO clienteDTO) {
        return Cliente.builder()
                .nombre(clienteDTO.getNombre())
                .apellido(clienteDTO.getApellido())
                .telefono(clienteDTO.getTelefono())
                .activo(clienteDTO.getActivo())
                .usuario(usuarioRepository.findById(clienteDTO.getUsuario().getId())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado")))
                .build();
    }
    public ClienteDTO toDto(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .telefono(cliente.getTelefono())
                .activo(cliente.getActivo())
                .usuario(usuarioService.toDto(cliente.getUsuario()))
                .build();
    }
}
