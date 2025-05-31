package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends GenericoMapper<Usuario, UsuarioDTO, UsuarioResponseDTO> {

    Usuario toEntity(UsuarioDTO usuarioDTO);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

}
