package org.spdgrupo.elbuensaborapi.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.UsuarioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Rol;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.repository.RolRepository;
import org.spdgrupo.elbuensaborapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final ManagementAPI managementAPI;
    private final RolRepository rolRepository;

    @Transactional
    public Usuario save (UsuarioDTO usuarioDTO) throws Auth0Exception {
        Set<Rol> roles = assignRoles(usuarioDTO.getRoles());

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

        // auth0
        User usuarioAuth0 = new User();
        usuarioAuth0.setEmail(usuarioDTO.getEmail());
        usuarioAuth0.setName(usuarioDTO.getNombreCompleto());
        if (usuarioDTO.getContrasenia() != null) {
            usuarioAuth0.setPassword(usuarioDTO.getContrasenia().toCharArray());
        }
        usuarioAuth0.setEmailVerified(true);
        usuarioAuth0.setConnection(usuarioDTO.getConnection());

        User createdUser = managementAPI.users().create(usuarioAuth0).execute();
        usuario.setAuth0Id(createdUser.getId());
        usuario.setRoles(roles);

        try {
            usuarioRepository.save(usuario);
            managementAPI.users().addRoles(usuario.getAuth0Id(), usuarioDTO.getRoles()).execute();
        } catch (Exception e) {
            // Si falla en la BD, elimino el usuario en Auth0
            managementAPI.users().delete(usuarioAuth0.getId()).execute();
            throw e;
        }
        return usuario;
    }

    @Transactional
    public Usuario saveExistingUser(UsuarioDTO usuarioDTO) throws Auth0Exception {
        User usuarioAuth0 = managementAPI.users().get(usuarioDTO.getAuth0Id(), null).execute();
        if (usuarioAuth0 == null || usuarioAuth0.getEmail() == null) {
            throw new Auth0Exception("El usuario con el id " + usuarioDTO.getAuth0Id() + " no existe en Auth0");
        }
        Set<Rol> roles = assignRoles(usuarioDTO.getRoles());

        usuarioDTO.setNombreCompleto(usuarioAuth0.getName());
        usuarioDTO.setEmail(usuarioAuth0.getEmail());
        if (usuarioDTO.getNickName() != null) {
            usuarioDTO.setNickName(usuarioAuth0.getNickname());
        }
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setRoles(roles);

        try {
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            // Si falla en la BD, elimino el usuario en Auth0
            managementAPI.users().delete(usuarioAuth0.getId()).execute();
            throw e;
        }
        return usuario;

    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario con el id " + id + " no encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO findByAuth0Id (String auth0Id) {
        Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new NotFoundException("Usuario con el auth0Id " + auth0Id + " no encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario con el email " + email + " no encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }


    @Transactional
    public void update(String auth0Id, UsuarioDTO usuarioDTO) throws Auth0Exception {
        Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new NotFoundException("Usuario con el id " + auth0Id + " no encontrado"));

        User usuarioAuth0 = new User();
        boolean changed = false;

        if ((!usuario.getNombreCompleto().equals(usuarioDTO.getNombreCompleto())) && (!usuarioDTO.getNombreCompleto().trim().isEmpty())) {
            usuario.setNombreCompleto(usuarioDTO.getNombreCompleto());
            usuarioAuth0.setName(usuarioDTO.getNombreCompleto());
            changed = true;
        }

        if (usuarioDTO.getContrasenia() != null && !usuarioDTO.getContrasenia().trim().isEmpty()) {
            usuarioAuth0.setPassword(usuarioDTO.getContrasenia().toCharArray());
            changed = true;
        }

        Set<String> rolesActuales = usuario.getRoles().stream()
                .map(Rol::getAuth0RolId)
                .collect(Collectors.toSet());
        Set<String> rolesNuevos = new HashSet<>(usuarioDTO.getRoles());

        if (!rolesActuales.equals(rolesNuevos)) {
            usuario.setRoles(assignRoles(usuarioDTO.getRoles()));
            // Actualizar roles en Auth0
            managementAPI.users().removeRoles(auth0Id, new ArrayList<>(rolesActuales)).execute();
            managementAPI.users().addRoles(auth0Id, usuarioDTO.getRoles()).execute();
            changed = true;
        }

        if (changed) {
            managementAPI.users().update(auth0Id, usuarioAuth0).execute();
            usuarioRepository.save(usuario);
        }
    }

    @Transactional
    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario con el id " + id + " no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteByAuth0Id(String id) {
        Usuario usuario = usuarioRepository.findByAuth0Id(id)
                .orElseThrow(() -> new NotFoundException("Usuario con el auth0Id " + id + " no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletePhysicallyByAuth0Id(String id) throws Auth0Exception {
        Usuario usuario = usuarioRepository.findByAuth0Id(id)
                .orElseThrow(() -> new NotFoundException("Usuario con el auth0Id " + id + " no encontrado"));
        managementAPI.users().delete(id).execute();
        usuarioRepository.delete(usuario);
    }

    @Transactional
    public void toggleActivo(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario con el id " + id + " no encontrado"));
        usuario.setActivo(!usuario.getActivo());
        usuarioRepository.save(usuario);
    }

   private Set<Rol> assignRoles(List<String> auth0RolIds) {
       Set<Rol> roles = new HashSet<>();
       for (String rolId : auth0RolIds) {
           Rol rol = rolRepository.findByAuth0RolId(rolId)
                   .orElseThrow(() -> new NotFoundException("Rol con el auth0Id " + rolId + " no encontrado"));
           roles.add(rol);
       }
       return roles;
   }
}
