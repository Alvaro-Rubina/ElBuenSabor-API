package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.repository.UsuarioRepository;
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

    public UsuarioResponseDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con el id " + id + " no encontrado"));
        return toDTO(usuario);
    }

    public UsuarioResponseDTO getUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario con el email " + email + " no encontrado"));
        return toDTO(usuario);
    }

    public List<UsuarioResponseDTO> getAllUsuarios() {
         List<Usuario> usuarios = usuarioRepository.findAll();
         List<UsuarioResponseDTO> usuarioDTOs = new ArrayList<>();

         for (Usuario usuario : usuarios) {
                usuarioDTOs.add(toDTO(usuario));
         }

         return usuarioDTOs;
    }

    public void updateUsuario(Long id, UsuarioPatchDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con el id " + id + " no encontrado"));
        
        if (usuarioDTO.getContraseña() != null) {
            usuario.setContrasenia(usuarioDTO.getContraseña());
        }

        if (usuarioDTO.getRol() != null) {
            usuario.setRol(usuarioDTO.getRol());
        }

        usuarioRepository.save(usuario);
    }

    // MAPPERS
    private Usuario toEntity(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .email(usuarioDTO.getEmail())
                .contrasenia(usuarioDTO.getContraseña())
                .rol(usuarioDTO.getRol())
                .build();
    }
    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .auth0Id(usuario.getAuth0Id())
                .rol(usuario.getRol())
                .build();
    }
}
