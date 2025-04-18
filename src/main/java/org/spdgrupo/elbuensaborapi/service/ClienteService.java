package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;
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
    private UsuarioRepository usuarioRepository;

    public void saveCliente(ClienteDTO clienteDTO) {
        Cliente cliente = toEntity(clienteDTO);

        // me aseguro que el rol sea siempre cliente
        cliente.getUsuario().setRol(Rol.CLIENTE);
        cliente.setActivo(true);
        clienteRepository.save(cliente);
    }

    public void updateCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + id + " no encontrado"));

        if (!clienteDTO.getNombreCompleto().equals(cliente.getNombreCompleto())) {
            cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
        }
        if (!clienteDTO.getTelefono().equals(cliente.getTelefono())) {
            cliente.setTelefono(clienteDTO.getTelefono());
        }
        if (!clienteDTO.getActivo().equals(cliente.getActivo())) {
            cliente.setActivo(clienteDTO.getActivo());
        }
        if (!clienteDTO.getUsuario().getId().equals(cliente.getUsuario().getId())) {
            cliente.setUsuario(usuarioRepository.findById(clienteDTO.getUsuario().getId())
                    .orElseThrow(() -> new NotFoundException("Usuario con el id " + cliente.getUsuario().getId() + " no encontrado")));
        }

        // me aseguro que el rol sea siempre cliente aunque se haya cambiado por accidente
        cliente.getUsuario().setRol(Rol.CLIENTE);
        clienteRepository.save(cliente);
    }

    public ClienteDTO getClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el " +  id + " no encontrado"));
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
                .orElseThrow(() -> new NotFoundException("Cliente con el id" + id  + " no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }


    private Cliente toEntity(ClienteDTO clienteDTO) {
        return Cliente.builder()
                .nombreCompleto(clienteDTO.getNombreCompleto())
                .telefono(clienteDTO.getTelefono())
                .activo(clienteDTO.getActivo())
                .usuario(usuarioRepository.findById(clienteDTO.getUsuario().getId())
                        .orElseThrow(() -> new NotFoundException("Usuario con el id" + clienteDTO.getUsuario().getId() + " no encontrado")))
                .build();
    }
    public ClienteDTO toDto(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nombreCompleto(cliente.getNombreCompleto())
                .telefono(cliente.getTelefono())
                .activo(cliente.getActivo())
                .usuario(usuarioService.toDto(cliente.getUsuario()))
                .build();
    }
}
