package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClientePatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    // Dependencias
    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;
    private final DetalleDomicilioService detalleDomicilioService;

    public void saveCliente(ClienteDTO clienteDTO) {
        Cliente cliente = toEntity(clienteDTO);

        // Me aseguro que el rol sea siempre cliente.
        cliente.getUsuario().setRol(Rol.CLIENTE);
        clienteRepository.save(cliente);
    }

    public ClienteResponseDTO getClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el " +  id + " no encontrado"));
        return toDTO(cliente);
    }

    public List<ClienteResponseDTO> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteResponseDTO> clientesDTO = new ArrayList<>();

        for (Cliente cliente : clientes) {
            clientesDTO.add(toDTO(cliente));
        }
        return clientesDTO;
    }

    public void updateCliente(Long id, ClientePatchDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + id + " no encontrado"));

        if (clienteDTO.getNombreCompleto() != null) {
            cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
        }

        if (clienteDTO.getTelefono() != null) {
            cliente.setTelefono(clienteDTO.getTelefono());
        }

        if (clienteDTO.getActivo() != null) {
            cliente.setActivo(clienteDTO.getActivo());
        }

        // actualizo el usuario del cliente
        usuarioService.updateUsuario(cliente.getUsuario().getId(), clienteDTO.getUsuario());

        // me aseguro que el rol sea siempre cliente aunque se haya cambiado por accidente (o no)
        cliente.getUsuario().setRol(Rol.CLIENTE);
        clienteRepository.save(cliente);
    }

    public void deleteCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id" + id  + " no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    // MAPPERS
    private Cliente toEntity(ClienteDTO clienteDTO) {
        return Cliente.builder()
                .nombreCompleto(clienteDTO.getNombreCompleto())
                .telefono(clienteDTO.getTelefono())
                .activo(true)
                .usuario(usuarioService.saveUsuario(clienteDTO.getUsuario()))
                .build();
    }
    public ClienteResponseDTO toDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nombreCompleto(cliente.getNombreCompleto())
                .telefono(cliente.getTelefono())
                .activo(cliente.getActivo())
                .usuario(usuarioService.toDTO(cliente.getUsuario()))
                .detalleDomicilios(cliente.getDetalleDomicilios().stream()
                        .map(detalleDomicilioService::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
