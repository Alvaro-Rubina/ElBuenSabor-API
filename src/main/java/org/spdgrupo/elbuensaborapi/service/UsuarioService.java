package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario saveUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = toEntity(usuarioDTO);
        usuarioRepository.save(usuario);
        return usuario;
    }

    public UsuarioDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con el id " + id + " no encontrado"));
        return toDto(usuario);
    }

    public UsuarioDTO getUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario con el email " + email + " no encontrado"));
        return toDto(usuario);
    }

    public List<UsuarioDTO> getAllUsuarios() {
         List<Usuario> usuarios = usuarioRepository.findAll();
         List<UsuarioDTO> usuariosDTO = new ArrayList<>();

         for (Usuario usuario : usuarios) {
                usuariosDTO.add(toDto(usuario));
         }

         return usuariosDTO;
    }

    public void updateUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        if (!usuarioDTO.getEmail().equals(usuario.getEmail())) {
            usuario.setEmail(usuarioDTO.getEmail());
        }
        if (!usuarioDTO.getContraseña().equals(usuario.getContraseña())) {
            usuario.setContraseña(usuarioDTO.getContraseña());
        }
        if (!usuarioDTO.getAuth0Id().equals(usuario.getAuth0Id())) {
            usuario.setAuth0Id(usuarioDTO.getAuth0Id());
        }
        if (!usuarioDTO.getRol().equals(usuario.getRol())) {
            usuario.setRol(usuarioDTO.getRol());
        }
        usuarioRepository.save(usuario);

    }

    // MAPPERS
    private Usuario toEntity(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .email(usuarioDTO.getEmail())
                .contraseña(usuarioDTO.getContraseña())
                .auth0Id(usuarioDTO.getAuth0Id())
                .rol(usuarioDTO.getRol())
                .build();
    }
    public UsuarioDTO toDto(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .contraseña(usuario.getContraseña())
                .auth0Id(usuario.getAuth0Id())
                .rol(usuario.getRol())
                .build();
    }
}
