package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.mappers.UsuarioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService extends GenericoServiceImpl<Usuario, UsuarioDTO, UsuarioResponseDTO, Long> {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioMapper usuarioMapper;

    public UsuarioService(GenericoRepository<Usuario, Long> genericoRepository, GenericoMapper<Usuario, UsuarioDTO, UsuarioResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public Usuario save(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        return usuarioRepository.save(usuario);
    }

    public UsuarioResponseDTO getUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario con el email " + email + " no encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public void update(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con el id " + id + " no encontrado"));

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setContrasenia(usuarioDTO.getContrasenia());
        usuario.setRol(usuarioDTO.getRol());

        usuarioRepository.save(usuario);
    }
}
