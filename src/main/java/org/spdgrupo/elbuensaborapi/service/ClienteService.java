package org.spdgrupo.elbuensaborapi.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
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
            System.out.println("SE EJECUTÓ EL METODO SAVE NORMAL");
        } else {
            usuario = usuarioService.saveExistingUser(clienteDTO.getUsuario());
            System.out.println("SE EJECUTÓ EL METODO SAVEEXISTINGUSER");
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

        if ((!cliente.getNombreCompleto().equals(clienteDTO.getNombreCompleto())) && (!clienteDTO.getNombreCompleto().trim().isEmpty())) {
            cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
            clienteDTO.getUsuario().setNombreCompleto(cliente.getNombreCompleto());
        }

        if ((!cliente.getTelefono().equals(clienteDTO.getTelefono())) && (!clienteDTO.getTelefono().trim().isEmpty())) {
            cliente.setTelefono(clienteDTO.getTelefono());
        }

        usuarioService.update(cliente.getUsuario().getAuth0Id(), clienteDTO.getUsuario());
        return clienteMapper.toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO updateByAuth0Id(String auth0Id, ClienteDTO clienteDTO) throws Auth0Exception {
        Cliente cliente = clienteRepository.findByUsuario_Auth0Id(auth0Id)
                .orElseThrow(() -> new NotFoundException("Cliente con el auth0Id " + auth0Id + " no encontrado"));

        User usuarioAuth0 = managementAPI.users().get(auth0Id, null).execute();
        boolean isSocial = usuarioAuth0.getIdentities() != null &&
                usuarioAuth0.getIdentities().stream()
                        .anyMatch(identity -> !identity.getConnection().equals("Username-Password-Authentication"));

        if ((!cliente.getNombreCompleto().equals(clienteDTO.getNombreCompleto()))
                && (!clienteDTO.getNombreCompleto().trim().isEmpty()) && !isSocial) {
            cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
            cliente.getUsuario().setNombreCompleto(clienteDTO.getNombreCompleto());
        }

        if ((!cliente.getTelefono().equals(clienteDTO.getTelefono())) && (!clienteDTO.getTelefono().trim().isEmpty())) {
            cliente.setTelefono(clienteDTO.getTelefono());
        }

        if (!isSocial) {
            usuarioService.update(cliente.getUsuario().getAuth0Id(), clienteDTO.getUsuario());
        }
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
        boolean nuevoEstado = !cliente.getActivo();
        cliente.setActivo(!cliente.getActivo());
        usuarioService.toggleActivo(cliente.getUsuario().getId());
        clienteRepository.save(cliente);

        // Actualiza el estado bloqueado en Auth0
        try {
            User userUpdate = new User();
            userUpdate.setBlocked(!nuevoEstado); // Si está inactivo, bloquea; si está activo, desbloquea
            managementAPI.users().update(cliente.getUsuario().getAuth0Id(), userUpdate).execute();
        } catch (Auth0Exception e) {
            throw new RuntimeException("Error al actualizar el estado en Auth0", e);
        }
    }
}
