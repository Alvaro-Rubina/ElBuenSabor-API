package org.spdgrupo.elbuensaborapi.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.ClienteMapper;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    // Dependencias
    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;
    private final ClienteMapper clienteMapper;
    private final ManagementAPI managementAPI;

    @Transactional
    public ClienteResponseDTO save(ClienteDTO clienteDTO) throws Auth0Exception {
        clienteDTO.getUsuario().setNombreCompleto(clienteDTO.getNombreCompleto());

        Usuario usuario;

        if (clienteDTO.getUsuario().getAuth0Id() == null) {
            usuario = usuarioService.save(clienteDTO.getUsuario());
        } else {
            usuario = usuarioService.saveExistingUser(clienteDTO.getUsuario());
        }

        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente.setUsuario(usuario);

        try {
            clienteRepository.save(cliente);
            return clienteMapper.toResponseDTO(cliente);
        } catch (Exception e) {
            managementAPI.users().delete(usuario.getAuth0Id()).execute();
            throw e;
        }
    }

    @Transactional
    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + id + " no encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO findByEmail(String email) {
        Cliente cliente = clienteRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new NotFoundException("Cliente con el email " + email + " no encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO findByAuth0Id(String auth0Id) {
        Cliente cliente = clienteRepository.findByUsuario_Auth0Id(auth0Id)
                .orElseThrow(() -> new NotFoundException("Cliente con el auth0Id " + auth0Id + " no encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public ClienteResponseDTO update(Long id, ClienteDTO clienteDTO) throws Auth0Exception {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + id + " no encontrado"));

        cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
        cliente.setTelefono(clienteDTO.getTelefono());

        usuarioService.update(cliente.getUsuario().getAuth0Id(), clienteDTO.getUsuario());
       return clienteMapper.toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id" + id  + " no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void deletePhysically(Long id) throws Auth0Exception {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + id + " no encontrado"));
        usuarioService.deletePhysicallyByAuth0Id(cliente.getUsuario().getAuth0Id());
        clienteRepository.delete(cliente);
    }

    @Transactional
    public void toggleActivo(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + id + " no encontrado"));
        cliente.setActivo(!cliente.getActivo());
        usuarioService.toggleActivo(cliente.getUsuario().getId());
        clienteRepository.save(cliente);
    }
}
