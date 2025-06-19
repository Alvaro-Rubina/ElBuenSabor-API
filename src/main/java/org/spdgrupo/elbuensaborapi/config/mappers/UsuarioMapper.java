package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.usuario.UsuarioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Rol;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = RolMapper.class)
public interface UsuarioMapper extends GenericoMapper<Usuario, UsuarioDTO, UsuarioResponseDTO> {

    @Mapping(target = "roles", source = "roles")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @Mapping(target = "roles", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);
}