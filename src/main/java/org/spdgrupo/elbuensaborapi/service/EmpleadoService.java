package org.spdgrupo.elbuensaborapi.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.EmpleadoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Empleado;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService{

    // Dependencias
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioService usuarioService;
    private final EmpleadoMapper empleadoMapper;
    private final ManagementAPI managementAPI;

    @Transactional
    public EmpleadoResponseDTO save(EmpleadoDTO empleadoDTO) throws Auth0Exception {
        empleadoDTO.getUsuario().setNombreCompleto(empleadoDTO.getNombreCompleto());

        Usuario usuario;

        if (empleadoDTO.getUsuario().getAuth0Id() == null) {
            usuario = usuarioService.save(empleadoDTO.getUsuario());
        } else {
            usuario = usuarioService.saveExistingUser(empleadoDTO.getUsuario());
        }
        
        Empleado empleado = empleadoMapper.toEntity(empleadoDTO);
        empleado.setUsuario(usuario);

        try {
            empleadoRepository.save(empleado);
            return empleadoMapper.toResponseDTO(empleado);
        } catch (Exception e) {
            managementAPI.users().delete(usuario.getAuth0Id()).execute();
            throw e;
        }
    }

    @Transactional
    public EmpleadoResponseDTO update(Long id, EmpleadoDTO empleadoDTO) throws Auth0Exception {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el " + id + " no encontrado"));

        if ((!empleado.getNombreCompleto().equals(empleadoDTO.getNombreCompleto())) && (!empleadoDTO.getNombreCompleto().trim().isEmpty())) {
            empleado.setNombreCompleto(empleadoDTO.getNombreCompleto());
            empleadoDTO.getUsuario().setNombreCompleto(empleadoDTO.getNombreCompleto());
        }

        if ((!empleado.getTelefono().equals(empleadoDTO.getTelefono())) && (!empleadoDTO.getTelefono().trim().isEmpty())) {
            empleado.setTelefono(empleadoDTO.getTelefono());
        }

        // TODO: Ver si esto lo dejo acá
        /*domicilioService.update(empleado.getDomicilio().getId(), empleadoDTO.getDomicilio());*/

        usuarioService.update(empleado.getUsuario().getAuth0Id(), empleadoDTO.getUsuario());
        return empleadoMapper.toResponseDTO(empleadoRepository.save(empleado));
    }

    @Transactional
    public EmpleadoResponseDTO updateByAuth0Id(String auth0Id, EmpleadoDTO empleadoDTO) throws Auth0Exception {
        Empleado empleado = empleadoRepository.findByUsuario_Auth0Id(auth0Id)
                .orElseThrow(() -> new NotFoundException("Cliente con el auth0Id " + auth0Id + " no encontrado"));

        User usuarioAuth0 = managementAPI.users().get(auth0Id, null).execute();
        boolean isSocial = usuarioAuth0.getIdentities() != null &&
                usuarioAuth0.getIdentities().stream()
                        .anyMatch(identity -> !identity.getConnection().equals("Username-Password-Authentication"));

        if ((!empleado.getNombreCompleto().equals(empleadoDTO.getNombreCompleto()))
                && (!empleadoDTO.getNombreCompleto().trim().isEmpty()) && !isSocial) {
            empleado.setNombreCompleto(empleadoDTO.getNombreCompleto());
            empleadoDTO.getUsuario().setNombreCompleto(empleado.getNombreCompleto());
        }

        if ((!empleado.getTelefono().equals(empleadoDTO.getTelefono())) && (!empleadoDTO.getTelefono().trim().isEmpty())) {
            empleado.setTelefono(empleadoDTO.getTelefono());
        }

        if (!isSocial) {
            usuarioService.update(empleado.getUsuario().getAuth0Id(), empleadoDTO.getUsuario());
        }
        return empleadoMapper.toResponseDTO(empleadoRepository.save(empleado));
    }

    public EmpleadoResponseDTO findById(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el id " + id + " no encontrado"));
        return empleadoMapper.toResponseDTO(empleado);
    }

    public EmpleadoResponseDTO findByEmail(String email) {
        Empleado empleado = empleadoRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new NotFoundException("Empleado con el email " + email + " no encontrado"));
        return empleadoMapper.toResponseDTO(empleado);
    }

    public List<EmpleadoResponseDTO> findAll() {
        return empleadoRepository.findAll().stream()
                .map(empleadoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el id" + id  + " no encontrado"));
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
    }

    @Transactional
    public void deletePhysically(Long id) throws Auth0Exception {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el id " + id + " no encontrado"));
        usuarioService.deletePhysicallyByAuth0Id(empleado.getUsuario().getAuth0Id());
        empleadoRepository.delete(empleado);
    }

    @Transactional
    public void toggleActivo(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el id " + id + " no encontrado"));
        empleado.setActivo(!empleado.getActivo());
        usuarioService.toggleActivo(empleado.getUsuario().getId());
        empleadoRepository.save(empleado);
    }
}
