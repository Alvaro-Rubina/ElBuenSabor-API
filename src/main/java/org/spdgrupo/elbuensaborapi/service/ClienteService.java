package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.ClienteMapper;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClientePatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    // Dependencias
    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;
    private final DetalleDomicilioService detalleDomicilioService;
    private final ClienteMapper clienteMapper;

    public void saveCliente(ClienteDTO clienteDTO) {
        clienteDTO.getUsuario().setRol(Rol.CLIENTE);
        Usuario usuario = usuarioService.saveUsuario(clienteDTO.getUsuario());

        Cliente cliente = clienteMapper.toEntity(clienteDTO);

        cliente.setUsuario(usuario);

        clienteRepository.save(cliente);
    }

    public ClienteResponseDTO getClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el " +  id + " no encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    public List<ClienteResponseDTO> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void updateCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + id + " no encontrado"));

        cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setActivo(clienteDTO.getActivo());

        clienteDTO.getUsuario().setRol(Rol.CLIENTE);
        usuarioService.updateUsuario(cliente.getUsuario().getId(), clienteDTO.getUsuario());

        clienteRepository.save(cliente);
    }

    public void deleteCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id" + id  + " no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
}
