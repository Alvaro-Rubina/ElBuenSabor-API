package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void saveUsuario(UsuarioDTO usuarioDTO){

        Usuario usuario = toEntity(usuarioDTO);
        usuarioRepository.save(usuario);

    }

    public UsuarioDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.getElementById(id);
        return toDto(usuario);
    }

    public List<UsuarioDTO> getAllUsuarios() {
         List<Usuario> usuarios = usuarioRepository.findAll();
         List<UsuarioDTO> usuariosDTO = new ArrayList<UsuarioDTO>();

         for (Usuario usuario : usuarios) {
                usuariosDTO.add(toDto(usuario));
         }

         return usuariosDTO;
    }

    public void updateUsuario(UsuarioDTO usuarioDTO, Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        if (!usuarioDTO.getNombreUsuario().equals(usuario.getNombreUsuario())) {
            usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
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
                .nombreUsuario(usuarioDTO.getNombreUsuario())
                .contraseña(usuarioDTO.getContraseña())
                .auth0Id(usuarioDTO.getAuth0Id())
                .rol(usuarioDTO.getRol())
                .build();
    }
    public UsuarioDTO toDto(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .contraseña(usuario.getContraseña())
                .auth0Id(usuario.getAuth0Id())
                .rol(usuario.getRol())
                .build();
    }
}
